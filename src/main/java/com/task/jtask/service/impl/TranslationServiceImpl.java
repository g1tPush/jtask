package com.task.jtask.service.impl;

import com.task.jtask.dto.TranslationDto;
import com.task.jtask.entity.TranslationRequestInfo;
import com.task.jtask.exceptions.GlobalException;
import com.task.jtask.repository.TranslationRepository;
import com.task.jtask.response.TranslationResponse;
import com.task.jtask.service.TranslationRequestService;
import com.task.jtask.service.TranslationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRequestService translationRequestService;
    private final TranslationRepository translationRepository;
    private final int threadPoolSize = 10;
    private final ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

    public TranslationServiceImpl(TranslationRepository translationRepository, TranslationRequestService translationRequestService) {
        this.translationRepository = translationRepository;
        this.translationRequestService = translationRequestService;
    }

    public TranslationRequestInfo translate(TranslationDto translationDto, String ipAddress) {
        String translatedText = translateText(translationDto);

        TranslationRequestInfo translationRequestInfo = TranslationRequestInfo.builder()
                .ipAddress(ipAddress)
                .originalStringToTranslate(translationDto.getText())
                .translatedString(translatedText)
                .build();

        return translationRepository.saveTranslation(translationRequestInfo);
    }

    private String translateText(TranslationDto translationDto) {
        List<String> words = Arrays.stream(translationDto.getText().split(" "))
                .filter(word -> !word.isEmpty())
                .toList();

        List<CompletableFuture<TranslationResponse>> futures = new ArrayList<>(Collections.nCopies(words.size(), null));

        for (int i = 0; i < words.size(); i++) {
            TranslationDto translationDtoText = TranslationDto.builder()
                    .text(words.get(i))
                    .targetLanguageCode(translationDto.getTargetLanguageCode())
                    .sourceLanguageCode(translationDto.getSourceLanguageCode())
                    .build();

            futures.set(i, CompletableFuture.supplyAsync(() -> translationRequestService.translate(translationDtoText), executorService));
        }

        StringBuilder translatedText = new StringBuilder();

        CompletableFuture.allOf(futures.get(0)).join();

        for (CompletableFuture<TranslationResponse> future : futures) {
            try {
                translatedText.append(future.get().getTranslations().get(0).getText()).append(" ");
            } catch (InterruptedException | ExecutionException e) {
                throw new GlobalException(e.getMessage());
            }
        }

        return translatedText.toString().trim();
    }

}
