package com.app.homecash.service.impl;

import com.app.homecash.domain.CreditCard;
import com.app.homecash.domain.HouseMember;
import com.app.homecash.domain.Role;
import com.app.homecash.dto.request.CreateCreditCardRequest;
import com.app.homecash.dto.response.CreditCardResponse;
import com.app.homecash.mapper.CreditCardMapper;
import com.app.homecash.repository.CreditCardRepository;
import com.app.homecash.repository.HouseMemberRepository;
import com.app.homecash.repository.UserRepository;
import com.app.homecash.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final HouseMemberRepository houseMemberRepository;
    private final UserRepository userRepository;
    private final CreditCardMapper creditCardMapper;

    @Override
    @Transactional
    public CreditCardResponse create(CreateCreditCardRequest request, Long userId) {
        // Domain rule: Only owner can edit or remove (create is also restricted to owners)
        if (!isUserOwner(userId)) {
            throw new IllegalStateException("Only house owners can create credit cards");
        }

        // Validate user exists and is active
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (!user.getActive()) {
            throw new IllegalStateException("Cannot create credit card for inactive user");
        }

        // Map DTO to entity
        CreditCard creditCard = creditCardMapper.toEntity(request);
        creditCard.setUserId(userId);

        creditCard = creditCardRepository.save(creditCard);

        return creditCardMapper.toResponse(creditCard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditCardResponse> getByUser(Long userId) {
        // Always filter by authenticated user (multi-tenant)
        List<CreditCard> creditCards = creditCardRepository.findByUserId(userId);
        return creditCards.stream()
            .map(creditCardMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CreditCardResponse getById(Long cardId, Long userId) {
        CreditCard creditCard = creditCardRepository.findById(cardId)
            .orElseThrow(() -> new IllegalArgumentException("Credit card not found with id: " + cardId));

        // Validate user owns the credit card
        if (!creditCard.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User does not own this credit card");
        }

        return creditCardMapper.toResponse(creditCard);
    }

    @Override
    @Transactional
    public CreditCardResponse update(Long cardId, CreateCreditCardRequest request, Long userId) {
        CreditCard creditCard = creditCardRepository.findById(cardId)
            .orElseThrow(() -> new IllegalArgumentException("Credit card not found with id: " + cardId));

        // Validate user owns the credit card
        if (!creditCard.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User does not own this credit card");
        }

        // Domain rule: Only owner can edit
        if (!isUserOwner(userId)) {
            throw new IllegalStateException("Only house owners can update credit cards");
        }

        // Update credit card fields
        creditCard.setName(request.getName());
        creditCard.setBrand(request.getBrand());
        creditCard.setLastDigits(request.getLastDigits());
        creditCard.setLimitCents(request.getLimitCents());
        creditCard.setClosingDay(request.getClosingDay());
        creditCard.setDueDay(request.getDueDay());

        creditCard = creditCardRepository.save(creditCard);

        return creditCardMapper.toResponse(creditCard);
    }

    @Override
    @Transactional
    public void delete(Long cardId, Long userId) {
        CreditCard creditCard = creditCardRepository.findById(cardId)
            .orElseThrow(() -> new IllegalArgumentException("Credit card not found with id: " + cardId));

        // Validate user owns the credit card
        if (!creditCard.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User does not own this credit card");
        }

        // Domain rule: Only owner can remove
        if (!isUserOwner(userId)) {
            throw new IllegalStateException("Only house owners can delete credit cards");
        }

        creditCardRepository.delete(creditCard);
    }

    /**
     * Check if user is an owner in any house.
     *
     * @param userId user id
     * @return true if user is owner in at least one house, false otherwise
     */
    private boolean isUserOwner(Long userId) {
        List<HouseMember> houseMembers = houseMemberRepository.findByUserId(userId);
        return houseMembers.stream()
            .anyMatch(member -> member.getRole() == Role.OWNER);
    }
}

