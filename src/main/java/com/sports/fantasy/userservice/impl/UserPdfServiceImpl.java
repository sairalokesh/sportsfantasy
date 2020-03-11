package com.sports.fantasy.userservice.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sports.fantasy.dao.UserTeamDao;
import com.sports.fantasy.model.AmountEntries;
import com.sports.fantasy.model.GameParticipants;
import com.sports.fantasy.model.GameQuestions;
import com.sports.fantasy.model.UserSelectedTeamsScore;
import com.sports.fantasy.repository.GameParticipantRepository;
import com.sports.fantasy.userservice.UserPdfService;

@Service
@Transactional
public class UserPdfServiceImpl implements UserPdfService {

  @Autowired
  private UserTeamDao userTeamDao;
  @Autowired
  private GameParticipantRepository gameParticipantRepository;

  @Override
  public ByteArrayInputStream generateUserPdf(Long questionId, Long amountId, String gametype, GameQuestions gameQuestions, AmountEntries amountEntries) throws DocumentException {
    List<UserSelectedTeamsScore> userSelectedTeamsScores = userTeamDao.findUsersSeletedTeamsScoreByQuestionIdAndAmountId(questionId, amountId, gametype);
    List<GameParticipants> gameParticipants = gameParticipantRepository.getAllParticipantsByQuestionId(questionId);
    if (gameParticipants != null && !gameParticipants.isEmpty()) {
      Map<Long, String> participants = getGameParticipants(gameParticipants);
        return generateGameUserPdf(userSelectedTeamsScores, participants, gameQuestions, amountEntries);
    }
    return null;
  }

  private Map<Long, String> getGameParticipants(List<GameParticipants> gameParticipants) {
    Map<Long, String> participants = new HashMap<>();
    for (GameParticipants participant : gameParticipants) {
      participants.put(participant.getId(), participant.getParticipantName());
    }
    return participants;
  }

  private ByteArrayInputStream generateGameUserPdf(List<UserSelectedTeamsScore> userSelectedTeamsScores, Map<Long, String> participants, GameQuestions gameQuestions, AmountEntries amountEntries) throws DocumentException {
    Document document = new Document(PageSize.A2,10,10,10,10);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    PdfPTable header = new PdfPTable(2);
    header.setWidthPercentage(100);
    header.setWidths(new int[] {1, 1});
    Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.NORMAL, BaseColor.BLACK);

    PdfPCell headercell = new PdfPCell();
    headercell = new PdfPCell(new Phrase(gameQuestions.getQuestion(), headerFont));
    headercell.setHorizontalAlignment(Element.ALIGN_LEFT);
    headercell.setBackgroundColor(new BaseColor(254, 232, 195));
    headercell.setBorderWidthRight(0);
    headercell.setPaddingTop(15);
    headercell.setPaddingBottom(15);
    headercell.setPaddingLeft(10);
    header.addCell(headercell);

    headercell = new PdfPCell(new Paragraph("Entry : " + amountEntries.getAmount(), headerFont));
    headercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    headercell.setBackgroundColor(new BaseColor(254, 232, 195));
    headercell.setPaddingTop(15);
    headercell.setPaddingBottom(15);
    headercell.setPaddingRight(10);
    headercell.setBorderWidthLeft(0);
    header.addCell(headercell);
    
    PdfPTable table = new PdfPTable(12);
    table.setWidthPercentage(100);
    table.setWidths(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
    table.setSpacingBefore(10f);

    PdfPCell hcell = new PdfPCell();
    hcell = new PdfPCell(new Phrase("UserName", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingTop(10);
    hcell.setPaddingBottom(10);
    table.addCell(hcell);

    hcell = new PdfPCell(new Phrase("Capitan", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingTop(10);
    hcell.setPaddingBottom(10);
    table.addCell(hcell);

    hcell = new PdfPCell(new Phrase("ViceCaptian", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingTop(10);
    hcell.setPaddingBottom(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Suppoter", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingTop(10);
    hcell.setPaddingBottom(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Participant 1", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingBottom(10);
    hcell.setPaddingTop(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Participant 2", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingTop(10);
    hcell.setPaddingBottom(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Participant 3", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingBottom(10);
    hcell.setPaddingTop(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Participant 4", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingTop(10);
    hcell.setPaddingBottom(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Participant 5", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingBottom(10);
    hcell.setPaddingTop(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Participant 6", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingTop(10);
    hcell.setPaddingBottom(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Participant 7", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingBottom(10);
    hcell.setPaddingTop(10);
    table.addCell(hcell);
    
    hcell = new PdfPCell(new Phrase("Participant 8", headFont));
    hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    hcell.setPaddingBottom(10);
    hcell.setPaddingTop(10);
    table.addCell(hcell);

    for (UserSelectedTeamsScore selectedTeamsScore : userSelectedTeamsScores) {

      PdfPCell cell = new PdfPCell();

      cell = new PdfPCell(new Phrase(selectedTeamsScore.getUsername()));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingTop(10);
      cell.setPaddingBottom(10);
      table.addCell(cell);

      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getCaptainid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingTop(10);
      cell.setPaddingBottom(10);
      table.addCell(cell);

      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getVicecaptainid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingTop(10);
      cell.setPaddingBottom(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getSuppoterid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingBottom(10);
      cell.setPaddingTop(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getParticipantoneid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingTop(10);
      cell.setPaddingBottom(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getParticipanttwoid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingBottom(10);
      cell.setPaddingTop(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getParticipantthreeid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingTop(10);
      cell.setPaddingBottom(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getParticipantfourid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingBottom(10);
      cell.setPaddingTop(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getParticipantfiveid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingTop(10);
      cell.setPaddingBottom(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getParticipantsixid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingBottom(10);
      cell.setPaddingTop(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getParticipantsevenid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingBottom(10);
      cell.setPaddingTop(10);
      table.addCell(cell);
      
      cell = new PdfPCell(new Phrase(participants.get(selectedTeamsScore.getParticipanteightid())));
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPaddingBottom(10);
      cell.setPaddingTop(10);
      table.addCell(cell); 
    }

    PdfWriter.getInstance(document, out);
    document.open();
    document.add(header);
    document.add(table);
    document.close();

  return new ByteArrayInputStream(out.toByteArray());

  }
}
