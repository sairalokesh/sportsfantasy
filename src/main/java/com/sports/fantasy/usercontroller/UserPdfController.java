package com.sports.fantasy.usercontroller;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.itextpdf.text.DocumentException;
import com.sports.fantasy.userservice.UserPdfService;

@Controller
@RequestMapping(value = "/user")
public class UserPdfController {
  
  @Autowired
  private UserPdfService userPdfService;
  
  @RequestMapping(value = "/game/{gametype}/pdf/{questionId}/download/{amountId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<InputStreamResource> upcoming(@PathVariable String gametype, @PathVariable Long questionId, @PathVariable Long amountId, Principal principal, HttpServletResponse response) throws DocumentException{
    ByteArrayInputStream bis = userPdfService.generateUserPdf(questionId, amountId, gametype);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=gameparticipants.pdf");
    return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
  }

}
