package com.sports.fantasy.userservice;

import java.io.ByteArrayInputStream;
import com.itextpdf.text.DocumentException;

public interface UserPdfService {

  ByteArrayInputStream generateUserPdf(Long questionId, Long amountId, String gametype)  throws DocumentException;

}
