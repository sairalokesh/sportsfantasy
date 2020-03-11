package com.sports.fantasy.userservice;

import java.io.ByteArrayInputStream;
import com.itextpdf.text.DocumentException;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameQuestions;

public interface UserPdfService {

  ByteArrayInputStream generateUserPdf(Long questionId, Long amountId, String gametype, GameQuestions gameQuestions, AmountEntries amountEntries)  throws DocumentException;

}
