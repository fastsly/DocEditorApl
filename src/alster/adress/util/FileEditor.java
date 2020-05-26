package alster.adress.util;

//import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
import alster.adress.util.Beneficiari;


import org.apache.poi.util.StringUtil;
import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextSegment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import alster.adress.MainApp;
import alster.adress.model.Person;

import org.apache.poi.xwpf.usermodel.XWPFTableCell;


public class FileEditor  {

	private String inPath;
	private String outPath;
	private String excelPath;
	private String week1Out;
	private String week2Out;
	private String week3Out;
	private String week4Out;
	private String fileName;
	private MainApp mainApp;
	private SimpleDateFormat date;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	Date evalDate;
	Calendar evalDateCal = new GregorianCalendar();
	Date fAdaptatDate;
	Calendar fAdaptatDateCal = new GregorianCalendar();
	Date planIntervDate;
	Calendar planIntervDateCal = new GregorianCalendar();
	Date prgIntDate;
	Calendar prgIntDateCal = new GregorianCalendar();
	Date prgRecDate;
	Calendar prgRecDateCal = new GregorianCalendar();
	Date monitDate;
	Calendar monitDateCal = new GregorianCalendar();
	private List<Person> personData;
	String [] consi = {"gestionarea emotiilor","formarea autocontrolului","relatiile cu vecinii,prietenii",
			"acceptarea si gestionarea simptomatologiei bolii","gestionarea si structurarea functionala a timpului","formarea tolerantei la stimulii externi",
			"formarea sentimentului de responsabilitate","formarea abilitatilor, si strategiilor de coping adecvate"};

	public FileEditor (){

	}

	public void edit(boolean isfisa) throws Exception{

		///inPath= "C:\\Users\\Emil\\workspace\\DocEdit\\Docs\\";
		//outPath = "C:\\Users\\Emil\\workspace\\";
	if (mainApp.getPersonData() != null){
		personData =mainApp.getPersonData();
	}
	evalDate = dateFormat.parse("01.01.1999");
	fAdaptatDate = dateFormat.parse("01.01.1999");
	planIntervDate= dateFormat.parse("01.01.1999");
	prgIntDate = dateFormat.parse("01.01.1999");
	prgRecDate = dateFormat.parse("01.01.1999");
	monitDate = dateFormat.parse("01.01.1999");

    File folder = new File(inPath);
    File[] listOfFiles = folder.listFiles();
    File tempfolder;
    File[] templist;
    ArrayList<Beneficiari> benList= null;
    try {
    	benList = this.workOnExcell(getExcelPath());
    }catch(Exception e) {
    	System.out.println("we finally fuckin found our error: "+e);
    }
    
    //handling fisaMonit
    if (isfisa){
	    for (int i = 0; i < listOfFiles.length; i++) {
	       if (listOfFiles[i].isFile()) {
	    	   String s= listOfFiles[i].getPath();
	    	   s=s.replaceAll("\\\\","\\\\\\\\");
	    	   XWPFDocument doc = new XWPFDocument(OPCPackage.open(s));
	    	   String name = listOfFiles[i].getName();
	    	   name = name.split(" Fisa")[0];
	    	   System.out.println("The filename is: "+name);
	    	   try {
	    		   for( Beneficiari benef : benList) {
	    			   System.out.println("the name is "+benef.getName());
	    			   if (benef.getName().equals(name)) {
	    				   System.out.println("the name is "+benef.getName());
	    				   handleActivBenef(doc,benef.isWeek1(),benef.isWeek2(),benef.isWeek3(), benef.isWeek4());
	    			   }
	    		   }
	    		System.out.println("Now we handle doc...");
	    	   doc= handleDate(doc);
	    	   System.out.println("Finished Handle Date");
	    	   }catch(Exception e) {
	    		   System.out.println("we have an error in handling fisa monit at edit function: "+e);
	    	   }
	    	   System.out.println ("We are working on " + s);
	    	   
	    	   //Change data intocmirii and date in filename
	    	   String getDateString = listOfFiles[i].getName().substring(listOfFiles[i].getName().length()-15, listOfFiles[i].getName().length()-5);
	    	   monitDate = dateFormat.parse(getDateString);
	    	   System.out.println ("We are working on " + s+" and we are replacing "+monitDate);
	    	   fileName = listOfFiles[i].getName();
	    	   try{
	    		   Calendar oldDate = new GregorianCalendar();
	  	   	   		oldDate.setTime(monitDate);
	  	   	   		monitDateCal.setTime(monitDate);
	  	   	   		monitDateCal.add(Calendar.MONTH,1);

	  	   	   		fileName = fileName.replaceAll(dateFormat.format(oldDate.getTime()),dateFormat.format(monitDateCal.getTime()));

	  	   	   		Calendar newDate= new GregorianCalendar();
	  	   	   		newDate.setTime(monitDate);
	  	   	   		newDate.add(Calendar.MONTH,1);
	  	   	   		replaceInParagraphs(dateFormat.format(monitDate),dateFormat.format(newDate.getTime()),doc.getParagraphs());
	  	   	   System.out.println ("We finished data intocmirii");
	    	   } catch (Exception e) {
	        	   System.out.println("Exception at changin date on monitorizare: "+e);
			   }
	    	   //doc= handleAbsent(doc);
	    	   /*
	     	   for (Person p : personData ){
	     		   if (p.getConsiNr() >= 8 ){
	     			  doc = handleConsi(doc);
	     			  p.setConsiNr(0);
	     		   }
	     		   if (p.getConsiNr() < 8){
	     			   p.setConsiNr(p.getConsiNr()+1);
	     		   }

	     	   }*/
	    	  //doc = handleConsi(doc);
		   //	write(new FileOutputStream(fileToWorkOn.replaceAll("\\\\","\\\\\\\\")));
	     	   //FileOutputStream file= new FileOutputStream(outPath+"\\\\"+listOfFiles[i].getName());
	     	   try{
	    	   doc.write(new FileOutputStream(outPath+"\\\\"+fileName.replaceAll("\\\\","\\\\\\\\")));
	     	   }//file.close();
				catch (Exception e) {
					System.out.println("Exception at write file on monitorizare: "+e);
				}
	       }//else if (listOfFiles[i].isDirectory()) {
	       //}
	    }
    }//else{
//    	//handling others
//    	String fisaEvaluareToWorkOn="";
//    	String fAdaptatToWorkOn="";
//    	String planIntervToWorkOn="";
//    	String prgIntToWorkOn="";
//    	String prgRecToWorkOn="";
//
//
//
//    	for (int i = 0; i < listOfFiles.length; i++) {
// 	       if (listOfFiles[i].isDirectory()) {
// 	    	  tempfolder = new File(listOfFiles[i].getPath());
// 	    	  templist = tempfolder.listFiles();
// 	    	  //System.out.println("The directory is: "+listOfFiles[i].getPath());
//
// 	    	  for (int j = 0; j < templist.length; j++){
// 	    		 String getDateString = templist[j].getName().substring(templist[j].getName().length()-15, templist[j].getName().length()-5);
// 	    		 //System.out.println("The date is: "+getDateString);
// 	    		 String getTypeString = templist[j].getName().substring(0,14);
// 	    		 //System.out.println("The type is: "+getTypeString);
//
// 	    		 switch(getTypeString){
//		    	 case "fisa evaluare ":
//		    		 if (evalDate.before(dateFormat.parse(getDateString))){
//		    			 evalDate = dateFormat.parse(getDateString);
//		    			 fisaEvaluareToWorkOn = templist[j].getPath();
//		    		 }
//		    		 break;
//		    	 case "Format adaptat":
//		    		 if (fAdaptatDate.before(dateFormat.parse(getDateString))){
//		    			 fAdaptatDate = dateFormat.parse(getDateString);
//		    			 fAdaptatToWorkOn = templist[j].getPath();
//		    		 }
//		    		 break;
//		    	 case "Plan de interv":
//		    		 if (planIntervDate.before(dateFormat.parse(getDateString))){
//		    			 planIntervDate = dateFormat.parse(getDateString);
//		    			 planIntervToWorkOn = templist[j].getPath();
//		    		 }
//		    		 break;
//		    	 case "plan de interv":
//		    		 if (planIntervDate.before(dateFormat.parse(getDateString))){
//		    			 planIntervDate = dateFormat.parse(getDateString);
//		    			 planIntervToWorkOn = templist[j].getPath();
//		    		 }
//		    		 break;
//		    	 case "program de int":
//		    		 if (prgIntDate.before(dateFormat.parse(getDateString))){
//		    			 prgIntDate = dateFormat.parse(getDateString);
//		    			 prgIntToWorkOn = templist[j].getPath();
//		    		 }
//		    		 break;
//		    	 case "program de rec":
//		    		 if (prgRecDate.before(dateFormat.parse(getDateString))){
//		    			 prgRecDate = dateFormat.parse(getDateString);
//		    			 prgRecToWorkOn = templist[j].getPath();
//		    		 }
//		    		 break;
//
//		    	 }
//		    	 evalDateCal.setTime(evalDate);
//		    	 fAdaptatDateCal.setTime(fAdaptatDate);
//		    	 planIntervDateCal.setTime(planIntervDate);
//		    	 prgIntDateCal.setTime(prgIntDate);
//		    	 prgRecDateCal.setTime(prgRecDate);
//		    	 System.out.println("we set evaldate to "+dateFormat.format(evalDateCal.getTime()));
//		    	 System.out.println("we set funct date to "+dateFormat.format(prgRecDateCal.getTime()));
// 	    	  }
// 	    	 writeFile(evalDate,fisaEvaluareToWorkOn,dateFormat,1);
//	    	 writeFile(fAdaptatDate, fAdaptatToWorkOn, dateFormat, 2);
//	    	 writeFile(planIntervDate, planIntervToWorkOn, dateFormat, 3);
//	    	 writeFile(prgIntDate,prgIntToWorkOn,dateFormat, 4);
//	    	 writeFile(prgRecDate,prgRecToWorkOn,dateFormat, 5);
//
// 	       }
//    	}
//    }

}

	// type: 1-fisa eval; 2- format adaptat; 3-plan de interventie; 4-program de int soc; 5-program de reab funct
		private void writeFile(Date date, String fileToWorkOn, SimpleDateFormat dateFormat,int type){
		try {
	   	 Calendar oldDate = new GregorianCalendar();
	   	 oldDate.setTime(date);
	   	 XWPFDocument doc = new XWPFDocument(OPCPackage.open(fileToWorkOn.replaceAll("\\\\","\\\\\\\\")));
	     XWPFDocument tempDoc = doc;
	     //doc.close();
	   	 switch (type){
	   	 case 1:
	   		 handleFisaEv(tempDoc, date, dateFormat);
	   		 evalDateCal.add(Calendar.MONTH,6);
	   		 fileToWorkOn = fileToWorkOn.replaceAll(dateFormat.format(oldDate.getTime()),dateFormat.format(evalDateCal.getTime()));
	   		 System.out.println("fisa name and path: "+fileToWorkOn+" and the date "+dateFormat.format(evalDateCal.getTime())+" and the old date "+dateFormat.format(oldDate.getTime()));
	   		 break;
	   	 case 2:
	   		handleFAdaptat(tempDoc, date, dateFormat);
	   		fAdaptatDateCal.add(Calendar.MONTH,3);
	   		fileToWorkOn = fileToWorkOn.replaceAll(dateFormat.format(oldDate.getTime()),dateFormat.format(fAdaptatDateCal.getTime()));
	   		System.out.println("fisa name and path: "+fileToWorkOn+" and the date "+dateFormat.format(fAdaptatDateCal.getTime())+" and the old date "+dateFormat.format(oldDate.getTime()));
	   		break;
	   	 case 3:
	   		handlePlanInterv(doc, date, dateFormat);
	   		planIntervDateCal.add(Calendar.MONTH,3);
	   		fileToWorkOn = fileToWorkOn.replaceAll(dateFormat.format(oldDate.getTime()),dateFormat.format(planIntervDateCal.getTime()));
	   		System.out.println("fisa name and path: "+fileToWorkOn+" and the date "+dateFormat.format(planIntervDateCal.getTime())+" and the old date "+dateFormat.format(oldDate.getTime()));
	   		 break;
	   	 case 4:
	   		handleProgramSoc(doc, date, dateFormat);
	   		prgIntDateCal.add(Calendar.MONTH,3);
	   		fileToWorkOn = fileToWorkOn.replaceAll(dateFormat.format(oldDate.getTime()),dateFormat.format(prgIntDateCal.getTime()));
	   		System.out.println("fisa name and path: "+fileToWorkOn+" and the date "+dateFormat.format(prgIntDateCal.getTime())+" and the old date "+dateFormat.format(oldDate.getTime()));
	   		break;
	   	 case 5:
	   		handleProgramSoc(doc, date, dateFormat);
	   		prgRecDateCal.add(Calendar.MONTH,3);
	   		fileToWorkOn = fileToWorkOn.replaceAll(dateFormat.format(oldDate.getTime()),dateFormat.format(prgRecDateCal.getTime()));
	   		System.out.println("fisa name and path: "+fileToWorkOn+" and the date "+dateFormat.format(prgRecDateCal.getTime())+" and the old date "+dateFormat.format(oldDate.getTime()));
	   		break;

	   	 }
		 tempDoc.write(new FileOutputStream(fileToWorkOn.replaceAll("\\\\","\\\\\\\\")));
		 //tempDoc.close();

		} catch (Exception e) {
			System.out.println("Exception at handleconsi: "+e);
		}
	   }
		public XWPFDocument handleDate(XWPFDocument doc){

			String[] replace = {week1Out, week2Out, week3Out, week4Out};

			XWPFTableRow row1HandleDate = null;
			//for (int j=0; j<=1; j++){
				XWPFTable table = doc.getTableArray(0);
				row1HandleDate= table.getRow(1);
				for(int i=1; i<=4; i++){

					XWPFTableCell cell1HandleDate = row1HandleDate.getCell(i);
					replaceDate(cell1HandleDate.getText(),replace[i-1],cell1HandleDate.getParagraphs(),false,false);
				}
			//}

			return doc;
	}
		
	public void handleActivBenef(XWPFDocument doc,Boolean week1,Boolean week2,Boolean week3,Boolean week4){
		XWPFTableRow row1 = null;
		//for (int j=0; j<=1; j++){
		XWPFTable table = doc.getTableArray(0);
		Random rand =new Random();
		XWPFTableCell tempCell = null;
		List<XWPFParagraph> tempParagraphs = null;
		for(int i=0; i<table.getNumberOfRows(); i++){
			row1= table.getRow(i);
			int cellcount =0;
			List<XWPFTableCell> list = row1.getTableCells();
			for(XWPFTableCell cell: list) {
				cellcount++;
				List<XWPFParagraph> xwpfParagraphs = cell.getParagraphs();
				for (XWPFParagraph paragraph : xwpfParagraphs) {
					
					String code = null;
					System.out.println("paragraph at row "+i+" cell "+cellcount+" is: "+paragraph.getParagraphText());
					
					try {
						if(paragraph.getParagraphText().length()>3) {
							code = paragraph.getParagraphText().substring(paragraph.getParagraphText().length()-3,paragraph.getParagraphText().length());
						}else {
							code = "";
						}
					}catch(Exception e) {
						System.out.println("error in handleActivBenef at getting code from string"+e);
					}
					if(code.equals("#01")||code.equals("#02")||code.equals("#03")||code.equals("#04")) {
						System.out.println("cell 1: "+list.get(1).getText()+"cell 2: "+list.get(2).getText()+"cell 3: "+list.get(3).getText()+"cell 4: "+list.get(4).getText());
					}
//					mySwitch(week1, week2, week3, week4, list, code,"#01",1);
//					mySwitch(week1, week2, week3, week4, list, code,"#02",2);
//					mySwitch(week1, week2, week3, week4, list, code,"#03",0);
//					mySwitch(week1, week2, week3, week4, list, code,"#04",0);
//					mySwitch(week1, week2, week3, week4, list, code,"#05",0);
//					mySwitch(week1, week2, week3, week4, list, code,"#06",0);
//					mySwitch(week1, week2, week3, week4, list, code,"#07",0);
//					mySwitch(week1, week2, week3, week4, list, code,"#08",0);
					mySwitch(week1, week2, week3, week4, list, code,"#01",4);
					mySwitch(week1, week2, week3, week4, list, code,"#02",4);
					mySwitch(week1, week2, week3, week4, list, code,"#03",4);
					mySwitch(week1, week2, week3, week4, list, code,"#04",4);
					mySwitch(week1, week2, week3, week4, list, code,"#05",3);
					mySwitch(week1, week2, week3, week4, list, code,"#06",3);
					mySwitch(week1, week2, week3, week4, list, code,"#07",2);
					mySwitch(week1, week2, week3, week4, list, code,"#08",2);
					mySwitch(week1, week2, week3, week4, list, code,"#09",1);
					mySwitch(week1, week2, week3, week4, list, code,"#10",1);
					mySwitch(week1, week2, week3, week4, list, code,"#11",4);
					mySwitch(week1, week2, week3, week4, list, code,"#12",4);
					mySwitch(week1, week2, week3, week4, list, code,"#13",4);
					mySwitch(week1, week2, week3, week4, list, code,"#14",4);
					mySwitch(week1, week2, week3, week4, list, code,"#15",1);
					mySwitch(week1, week2, week3, week4, list, code,"#16",4);
					mySwitch(week1, week2, week3, week4, list, code,"#17",4);
					mySwitch(week1, week2, week3, week4, list, code,"#18",4);
					mySwitch(week1, week2, week3, week4, list, code,"#19",1);
					mySwitch(week1, week2, week3, week4, list, code,"#20",4);
					mySwitch(week1, week2, week3, week4, list, code,"#21",4);
					mySwitch(week1, week2, week3, week4, list, code,"#22",4);
					mySwitch(week1, week2, week3, week4, list, code,"#23",1);
					mySwitch(week1, week2, week3, week4, list, code,"#24",1);
					mySwitch(week1, week2, week3, week4, list, code,"#25",0);
					mySwitch(week1, week2, week3, week4, list, code,"#26",0);
					mySwitch(week1, week2, week3, week4, list, code,"#27",0);
					mySwitch(week1, week2, week3, week4, list, code,"#28",2);
					mySwitch(week1, week2, week3, week4, list, code,"#29",4);
					mySwitch(week1, week2, week3, week4, list, code,"#30",4);
					mySwitch(week1, week2, week3, week4, list, code,"#31",1);
					mySwitch(week1, week2, week3, week4, list, code,"#32",1);
					mySwitch(week1, week2, week3, week4, list, code,"#33",4);
				}
			}
			
			//replaceDate(cell1.getText(),replace[i-1],cell1.getParagraphs(),false);
		}
		//}

			//return doc;
	}

	private void mySwitch(Boolean week1, Boolean week2, Boolean week3, Boolean week4, List<XWPFTableCell> list,
			String code, String codePreDetermined, int countMax) {
		XWPFTableCell tempCell;
		List<XWPFParagraph> tempParagraphs;
		int nrPrezente = 0;
		if (week1) {nrPrezente++;}
		if (week2) {nrPrezente++;}
		if (week3) {nrPrezente++;}
		if (week4) {nrPrezente++;}
		//int lastRand = 5;
		List<Integer> numbers = new ArrayList<Integer>();
        int  numberOfNumbersYouWant = 4; // This has to be less than 11
        Random random = new Random();
        do
        {
            int next = random.nextInt(4)+1;
            if (!numbers.contains(next))
            {
                numbers.add(next);
                //System.out.println(next);
            }
        } while (numbers.size() < numberOfNumbersYouWant);
        
		int count =0;
		if(code.equals(codePreDetermined)) {		
			System.out.println("the code we are tryin is: "+code +" against the predetermined "+ codePreDetermined);
			for(XWPFTableCell asd : list) {
				System.out.println("We have cell number "+asd.getText());
			}
				for(int k=1; k<5; k++) {
					System.out.println("we delete existing");
					tempCell = list.get(k);
					tempParagraphs =tempCell.getParagraphs();
					replaceDate("x","",tempParagraphs, true,false);
					//tempCell.setText("");
				}
				if(countMax>0 && nrPrezente>0) {
					count= 0;
					int counter=0;
					do {
						int rand1 = numbers.get(counter);
						counter++;
//						do {
//						rand1=(int)(Math.random()*3+1);
//						}while (rand1!=lastRand);
//						lastRand = rand1;
						
						System.out.println("we found "+code+" and random is "+rand1);
						try {
							switch(rand1) {
							case 1:
								tempCell = list.get(1);
								tempParagraphs =tempCell.getParagraphs();
//								for(XWPFParagraph tempnew:tempParagraphs) {
//									System.out.println("we work on first week and random is "+rand1+" and paragraph is "+tempnew.getText()+";");
//								}
								//System.out.println("we work on first week");
								if(week1) {
									//replaceDate("","x",tempParagraphs,false,true);
									tempCell.setText("x");
									count++;
									System.out.println("we work on first week and count is:"+count);
								}else {
									//replaceInParagraphs("x","",tempParagraphs);
								}
								break;
							case 2:
								tempCell = list.get(2);
								tempParagraphs =tempCell.getParagraphs();
								if(week2) {
									//replaceDate("","x",tempParagraphs,false,true);
									tempCell.setText("x");
									count++;
									System.out.println("we work on second week and count is:"+count);
								}else {
									//replaceInParagraphs("x","",tempParagraphs);
								}
								break;
							case 3:
								tempCell = list.get(3);
								tempParagraphs =tempCell.getParagraphs();
								if(week3) {
									//replaceDate("","x",tempParagraphs,false,true);
									tempCell.setText("x");
									count++;
									System.out.println("we work on third week and count is:"+count);
								}else {
									//replaceInParagraphs("x","",tempParagraphs);
								}
								break;
							case 4:
								tempCell = list.get(4);
								tempParagraphs =tempCell.getParagraphs();
								if(week4) {
									//replaceDate("","x",tempParagraphs,false,true);
									tempCell.setText("x");
									count++;
									System.out.println("we work on fourth week and count is:"+count);
								}else {
									//replaceInParagraphs("x","",tempParagraphs);
								}
								break;
							default:
								System.out.println("we are shit out of luck they were missing the whole month");
								break;
								
						}
						}catch(Exception e) {
							System.out.println("error in handleactivbenef at second switch "+e);
						}
					}while(count<countMax && count<nrPrezente);
				}
			
		}
	}
		
	public XWPFDocument handleConsi(XWPFDocument doc){
	    	try{
	    		int rand = ThreadLocalRandom.current().nextInt(0, 8);
	    		System.out.println("ive handled consi");
	    		for(int i=0; i<5; i++){
	    			XWPFTable table = doc.getTableArray(1);
	    			XWPFTableRow row1= table.getRow(i);
	    			XWPFTableCell cell1 = row1.getCell(2);
	    			replaceInParagraphs(consi,cell1.getParagraphs(),rand);
	    		}
	    		return doc;
	    	}
	    	catch (Exception e){
	    		System.out.println("Exception at handleconsi: "+e);
	    		return null;
	    	}
	}

	public XWPFDocument handleFisaEv(XWPFDocument doc,Date date,SimpleDateFormat dateFormat){
		try{
			int rand = ThreadLocalRandom.current().nextInt(-3, 3);
			XWPFTable table = doc.getTableArray(0);
			XWPFTableRow row1= table.getRow(0);
			XWPFTableCell cell1 = row1.getCell(2);

			//we set the eval number
			String tempString = table.getRow(0).getCell(2).getText();
			int tempInt= Integer.parseInt(tempString.replaceAll("[\\D]", ""));
			replaceInParagraphs(tempString,"Reevaluare "+(tempInt+2),cell1.getParagraphs());
			cell1 = row1.getCell(3);
			replaceInParagraphs("Reevaluare "+(tempInt+1),"Reevaluare "+(tempInt+3),cell1.getParagraphs());

			//We set the dates
			Calendar newDate= new GregorianCalendar();
			newDate.setTime(date);
			newDate.add(Calendar.MONTH,3);
			Calendar oldDate= new GregorianCalendar();
			oldDate.setTime(date);

			row1= table.getRow(1);
			cell1=row1.getCell(2);
			oldDate.add(Calendar.MONTH,-3);
			replaceInParagraphs(dateFormat.format(oldDate.getTime()),dateFormat.format(newDate.getTime()),cell1.getParagraphs());

			cell1=row1.getCell(3);
			oldDate.add(Calendar.MONTH,3);
			newDate.add(Calendar.MONTH,3);
			replaceInParagraphs(dateFormat.format(oldDate.getTime()),dateFormat.format(newDate.getTime()),cell1.getParagraphs());

			//we set the weigth
			row1= table.getRow(4);
			cell1=row1.getCell(3);
			tempString = table.getRow(4).getCell(3).getText();
			tempInt= Integer.parseInt(tempString.replaceAll("[\\D]", ""));
			tempInt = (tempInt+rand);
			replaceInParagraphs(tempString,tempInt+" kg",cell1.getParagraphs());
			cell1=row1.getCell(5);
			tempString = table.getRow(4).getCell(5).getText();
			replaceInParagraphs(tempString,(tempInt+rand)+" kg",cell1.getParagraphs());

			return doc;

		}catch (Exception e){
			System.out.println("Exception at handlefisaev: "+e);
			return null;
		}

	}

	public XWPFDocument handleFAdaptat(XWPFDocument doc,Date date,SimpleDateFormat dateFormat){
		try{
			Calendar newDate= new GregorianCalendar();
			newDate.setTime(date);
			newDate.add(Calendar.MONTH,3);
			 replaceInParagraphs(dateFormat.format(date),dateFormat.format(newDate.getTime()),doc.getParagraphs());
		}
		catch (Exception e){
			System.out.println("Exception at handleFAdaptat: "+e);
			return null;
		}

		return doc;
	}

	public XWPFDocument handlePlanInterv(XWPFDocument doc,Date date,SimpleDateFormat dateFormat){
		try{
			Calendar newDate= new GregorianCalendar();
			newDate.setTime(date);
			newDate.add(Calendar.MONTH,3);
			Calendar nextDate= new GregorianCalendar();
			nextDate.setTime(date);
			nextDate.add(Calendar.MONTH,6);
			 replaceInParagraphs(dateFormat.format(newDate.getTime()),dateFormat.format(nextDate.getTime()),doc.getParagraphs());
			 replaceInParagraphs(dateFormat.format(date),dateFormat.format(newDate.getTime()),doc.getParagraphs());
			 DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			 LocalDate birthdate;
			 birthdate = LocalDate.parse("01.01.1979", format);
			 Instant instant = date.toInstant();
			 ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
			 LocalDate now = zdt.toLocalDate();
			 int ageOld= calculateAge(birthdate, now);
			 int ageNew= calculateAge(birthdate, now.plusMonths(3));
			 replaceInParagraphs("Varsta: "+ageOld,"Varsta: "+ageNew,doc.getParagraphs());
		}
		catch (Exception e){
			System.out.println("Exception at planinterv: "+e);
			return null;
		}

		return doc;
	}

	public XWPFDocument handleProgramSoc(XWPFDocument doc,Date date,SimpleDateFormat dateFormat){
		try{
			Calendar newDate= new GregorianCalendar();
			newDate.setTime(date);
			newDate.add(Calendar.MONTH,3);
			Calendar nextDate= new GregorianCalendar();
			nextDate.setTime(date);
			nextDate.add(Calendar.MONTH,6);


			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate birthdate;
			birthdate = LocalDate.parse("01.01.1979", format);
			Instant instant = date.toInstant();
			ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
			LocalDate now = zdt.toLocalDate();
			int ageOld= calculateAge(birthdate, now);
			int ageNew= calculateAge(birthdate, now.plusMonths(3));
			System.out.println("the ages are "+ageOld+" and "+ageNew);
			 replaceInParagraphs(dateFormat.format(newDate.getTime()),dateFormat.format(nextDate.getTime()),doc.getParagraphs());
			 replaceInParagraphs(dateFormat.format(date),dateFormat.format(newDate.getTime()),doc.getParagraphs());
			 replaceInParagraphs("VARSTA: "+ageOld,"VARSTA: "+ageNew,doc.getParagraphs());
		}
		catch (Exception e){
			System.out.println("Exception at handleFAdaptat: "+e);
			return null;
		}

		return doc;
	}

	public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
	    if ((birthDate != null) && (currentDate != null)) {
	        return Period.between(birthDate, currentDate).getYears();
	    } else {
	        return 0;
	    }
	}
	    public void replaceInParagraphs(String[] replacements, List<XWPFParagraph> xwpfParagraphs,int rand) {
	    	try{
	    		    for (XWPFParagraph paragraph : xwpfParagraphs) {
	    		      List<XWPFRun> runs = paragraph.getRuns();
	    		      for (String replPair : replacements) {
	    		        String find = replPair;
	    		        String repl = replacements[rand];
	    		        TextSegment found = paragraph.searchText(find, new PositionInParagraph());
	    		        if ( found != null ) {

	    		          if ( found.getBeginRun() == found.getEndRun() ) {
	    			            // whole search string is in one Run
	    			           XWPFRun run = runs.get(found.getBeginRun());
	    			           String runText = run.getText(run.getTextPosition());
	    			           System.out.println("the text is: "+runText);
	    			           String replaced = runText.replace(find, repl);
	    			           run.setText(replaced, 0);
	    		          } else {
	    			            // The search string spans over more than one Run
	    			            // Put the Strings together
	    			            StringBuilder b = new StringBuilder();
	    			            for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
	    				              XWPFRun run = runs.get(runPos);
	    				              b.append(run.getText(run.getTextPosition()));
	    			            }


	    			            String connectedRuns = b.toString();
	    			            System.out.println("the text is: "+connectedRuns);
	    			            if (connectedRuns == repl){
	    				            rand = ThreadLocalRandom.current().nextInt(0, 8);
	    				            repl = replacements[rand];
	    			            }
	    			            String replaced = connectedRuns.replace(find, repl);


	    			            // The first Run receives the replaced String of all connected Runs
	    			            XWPFRun partOne = runs.get(found.getBeginRun());
	    			            partOne.setText(replaced, 0);
	    			            // Removing the text in the other Runs.
	    			            for (int runPos = found.getBeginRun()+1; runPos <= found.getEndRun(); runPos++) {
	    				              XWPFRun partNext = runs.get(runPos);
	    				              partNext.setText("", 0);
	    			            }
	    		          }
	    		        }else{System.out.println("I havent found shit");
	    		      }
	    		      }
	    		    }
	    	}catch (Exception e){
	    		System.out.println("Exception at handleparagraph replacer: "+e);

	    	}
	    }
	    public void replaceInParagraphs(String find,String replacement, List<XWPFParagraph> xwpfParagraphs) {
	    	try{
	    		System.out.println("we started replacing"+" we replace "+find+" with "+replacement);
	    		    for (XWPFParagraph paragraph : xwpfParagraphs) {
	    		      List<XWPFRun> runs = paragraph.getRuns();
	    		        String repl = replacement;
	    		        TextSegment found = paragraph.searchText(find, new PositionInParagraph());
	    		        if ( found != null ) {
	    		          if ( found.getBeginRun() == found.getEndRun() ) {
	    			            // whole search string is in one Run
	    			           XWPFRun run = runs.get(found.getBeginRun());
	    			           String runText = run.getText(run.getTextPosition());
	    			           System.out.println("the text is(one run): "+runText+" we replace "+find+" with "+repl);
	    			           String replaced = runText.replace(find, repl);
	    			           run.setText(replaced, 0);
	    		          } else {
	    			            // The search string spans over more than one Run
	    			            // Put the Strings together
	    			            StringBuilder b = new StringBuilder();
	    			            for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
	    				              XWPFRun run = runs.get(runPos);
	    				              b.append(run.getText(run.getTextPosition()));
	    			            }


	    			            String connectedRuns = b.toString();
	    			            System.out.println("the text is: "+connectedRuns+" we replace "+find+" with "+repl);
	    			            String replaced = connectedRuns.replace(find, repl);


	    			            // The first Run receives the replaced String of all connected Runs
	    			            XWPFRun partOne = runs.get(found.getBeginRun());
	    			            partOne.setText(replaced, 0);
	    			            // Removing the text in the other Runs.
	    			            for (int runPos = found.getBeginRun()+1; runPos <= found.getEndRun(); runPos++) {
	    				              XWPFRun partNext = runs.get(runPos);
	    				              partNext.setText("", 0);
	    			            }
	    		          }
	    		        }else{System.out.println("we didnt find it");}

	    		    }
	    	}catch (Exception e){
	    		System.out.println("Exception at handleparagraph replacer: "+e);

	    	}
	    }


	    public void replaceDate(String find, String replacements, List<XWPFParagraph> xwpfParagraphs, boolean eraseAll, boolean create) {
	    	try{

	    		    for (XWPFParagraph paragraph : xwpfParagraphs) {
	    		    	List<XWPFRun> runs = paragraph.getRuns();
	    		    	if (create) {
	    		        	for (XWPFRun run: runs){
	    		        		run.setText(replacements, 0);
	    		        	}
	    		        }else {
	    		    	String repl = replacements;
	    		        TextSegment found = paragraph.searchText(find, new PositionInParagraph());
	    		        if ( found != null  ) {

	    		          if ( found.getBeginRun() == found.getEndRun() ) {
	    		            // whole search string is in one Run
	    		           XWPFRun run = runs.get(found.getBeginRun());
	    		           String runText = run.getText(run.getTextPosition());
	    		           String replaced = runText.replace(find, repl);
	    		           if (eraseAll){replaced=" ";}
	    		           run.setText(replaced, 0);
	    		           System.out.println("found text: "+runText+" and eraseall is "+eraseAll);
	    		          } else {
	    		            // The search string spans over more than one Run
	    		            // Put the Strings together
	    		            StringBuilder b = new StringBuilder();
	    		            for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
	    		              XWPFRun run = runs.get(runPos);
	    		              b.append(run.getText(run.getTextPosition()));
	    		            }

	    		            String connectedRuns = b.toString();
	    		            String replaced = connectedRuns.replace(find, repl);
	    		            if (eraseAll){replaced=" ";}
	    		            System.out.println("the text is: "+connectedRuns+" and eraseall is "+eraseAll);

	    		            // The first Run receives the replaced String of all connected Runs
	    		            XWPFRun partOne = runs.get(found.getBeginRun());
	    		            partOne.setText(replaced, 0);
	    		            // Removing the text in the other Runs.
	    		            for (int runPos = found.getBeginRun()+1; runPos <= found.getEndRun(); runPos++) {
	    		              XWPFRun partNext = runs.get(runPos);
	    		              partNext.setText("", 0);
	    		            }
	    		          }
	    		        }

	    		        if (eraseAll){
	    		        	for (XWPFRun run: runs){
	    		        		run.setText("", 0);
	    		        	}

	    		        }
	    		        }
	    		        
	    		      }

	    	}catch (Exception e){
	    		System.out.println("Exception at handleparagraph replacer: "+e);

	    	}
	    }


	public String getInPath() {
      return inPath;
    }

    public void setInPath(String s) {
        inPath=s;
    }
    public void setExcelPath(String s) {
        excelPath=s;
    }
    public String getExcelPath() {
        return excelPath;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String s) {
        outPath=s;
    }


    public String getWeek1Out() {
        return week1Out;
    }

    public void setWeek1Out(String s) {
        week1Out=s;
    }

    public String getWeek2Out() {
        return week2Out;
    }

    public void setWeek2Out(String s) {
        week2Out=s;
    }

    public String getWeek3Out() {
        return week3Out;
    }

    public void setWeek3Out(String s) {
        week3Out=s;
    }

    public String getWeek4Out() {
        return week4Out;
    }

    public void setWeek4Out(String s) {
        week4Out=s;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public ArrayList<Beneficiari> workOnExcell(String file) throws IOException {
    	File myFile = new File(file);
        
    	FileInputStream fis = new FileInputStream(myFile);

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
       
        // Return first sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
        String temp= getWeek1Out();
        temp=temp.substring(temp.length() - 5,temp.length() - 3);
        Integer week1 = Integer.valueOf(temp);
        
        temp= getWeek2Out();
        temp= temp.substring(temp.length() - 5,temp.length() - 3);
        Integer week2 = Integer.valueOf(temp);
        
        temp= getWeek3Out();
        temp=temp.substring(temp.length() - 5,temp.length() - 3);
        Integer week3 = Integer.valueOf(temp);
        
//        temp= getWeek4Out();
//        temp=temp.substring(temp.length() - 2);
//        Integer week4 = Integer.valueOf(temp);
        String name= "something's wrong m8";
        Boolean prezentWeek1=false;
        Boolean prezentWeek2=false;
        Boolean prezentWeek3=false;
        Boolean prezentWeek4= false;
        ArrayList<Beneficiari> list = new ArrayList<Beneficiari>(); 
        Row firstRow = mySheet.getRow(0);
        
        for(int i=1; i < 49;i++) {
        	Row row=mySheet.getRow(i);
        	prezentWeek1= false;
            prezentWeek2= false;
            prezentWeek3= false;
            prezentWeek4= false;
        	if (row.getCell(0).getStringCellValue().equals(null) || row.getCell(0).getStringCellValue().equals("")) {
        	}else {
        		for(int j=0; j < 23;j++) {
	        		if (j == 0) {
	       				name = row.getCell(j).getStringCellValue();
	        		}else {
	        			if(firstRow.getCell(j).getNumericCellValue()>week3) {
	        				if(row.getCell(j).getNumericCellValue() == 1) {
	        					prezentWeek4 =true;
	        				}
	        			}else
	        			if (firstRow.getCell(j).getNumericCellValue()>week2) {
	        				if(row.getCell(j).getNumericCellValue() == 1) {
	        					prezentWeek3 =true;
	        				}
	        			}else if(firstRow.getCell(j).getNumericCellValue()>week1) {
	        				if(row.getCell(j).getNumericCellValue() == 1) {
	        					prezentWeek2 =true;
	        				}
	        			}else {
	        				if(row.getCell(j).getNumericCellValue() == 1) {
	        					prezentWeek1 =true;
	        				}
	        			}
	        			
	        				
	        			
	        		}
        		}
        		Beneficiari ben = new Beneficiari(name,prezentWeek1, prezentWeek2,prezentWeek3, prezentWeek4);
        		System.out.println(ben.getName());
        		System.out.println(ben.isWeek1());
        		System.out.println(ben.isWeek2());
        		System.out.println(ben.isWeek3());
        		System.out.println(ben.isWeek4());
	        	list.add(ben);
        	}
        	
        }
        //myWorkBook.close();
        return list;
    }
}


