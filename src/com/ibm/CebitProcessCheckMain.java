package com.ibm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.AddressException;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class CebitProcessCheckMain {
	//private static ReadProperties props = new ReadProperties();
	public static void main(String[] args) throws Exception {
		
		String user = "AN533260AD";
		String pwd="Tktm$9NF3sTg6#745Jp#";
		

		String command ="ps -ef|grep bulk|grep -v grep";
		//String command1 = "tail -1000 /opt/IBM/FileNet/cebi/apps/BulkImport/cebit.log";
		String command_batchProc = "ps -ef|grep BatchProc|grep -v grep";
		String subject = "Cebit Process Validation - Green";
		String MailBody = "";
		String MailBodyNext = "\n";
		String MailBodySlash = "******************************************************************************************************";
		String MailBody127Server = "va10p10127 server (Cebit Process) ::";
		String MailBody300Server = "va10puvcbi300 server (Cebit Process) ::";
		String MailBody301Server = "va10puvcbi301 server (Cebit Process) ::";
		String MailBody018Server = "va10puvwbs018 server (Cebit Process) ::";
		String MailBody300ServerBatchProc = "va10puvcbi300 server (EEDI BatchProc) ::";
		String MailBody301ServerBatchProc = "va10puvcbi301 server (EEDI BatchProc) ::";
		
		//String MailBody300Log = "va10puvcbi300 server | Cebit Log  ::";
		//String MailBody301Log = "va10puvcbi301 server | Cebit Log  ::";
		
		String MailBody127 = CebitProcessCheck(user,pwd,"va10p10127",command);
		String MailBody300 = CebitProcessCheck(user,pwd,"va10puvcbi300",command);
		String MailBody301 = CebitProcessCheck(user,pwd,"va10puvcbi301",command);
		String MailBody018 = CebitProcessCheck(user,pwd,"va10puvwbs018",command);
		
		//String CebitLog300 = CebitProcessCheck(user,pwd,"va10puvcbi300",command1);
		//String CebitLog301 = CebitProcessCheck(user,pwd,"va10puvcbi301",command1);
		String BatchProc300 = CebitProcessCheck(user,pwd,"va10puvcbi300",command_batchProc);
		String BatchProc301 = CebitProcessCheck(user,pwd,"va10puvcbi301",command_batchProc);
		
		String MailBody1 = MailBody127Server+MailBodyNext+MailBodyNext+MailBody127+MailBodyNext+MailBodySlash+MailBodyNext;
		String MailBody2 = MailBody300Server+MailBodyNext+MailBodyNext+MailBody300+MailBodyNext+MailBodySlash+MailBodyNext;
		String MailBody3 = MailBody301Server+MailBodyNext+MailBodyNext+MailBody301+MailBodyNext+MailBodySlash+MailBodyNext;
		String MailBody4 = MailBody018Server+MailBodyNext+MailBodyNext+MailBody018+MailBodyNext+MailBodySlash+MailBodyNext;
		String MailBody5 = MailBody300ServerBatchProc+MailBodyNext+MailBodyNext+BatchProc300+MailBodyNext+MailBodySlash+MailBodyNext;
		String MailBody6 = MailBody301ServerBatchProc+MailBodyNext+MailBodyNext+BatchProc301+MailBodyNext+MailBodySlash+MailBodyNext;
		
		Boolean flag = true;
		if(MailBody127.contains("allcfgfiles.txt") && MailBody300.contains("allcfgfiles.txt") && BatchProc300.contains("BatchProc/SmartCred") && BatchProc300.contains("BatchProc/EEDI_Central") 
				&& BatchProc300.contains("BatchProc/EEDI_East") && BatchProc300.contains("BatchProc/EEDI_West") && MailBody301.contains("allcfgfiles.txt")
				 && MailBody018.contains("allcfgfiles.txt") && MailBody018.contains("westtime.txt") && MailBody018.contains("othercfg.txt") 
				 && MailBody018.contains("feptime.txt") && BatchProc301.contains("BatchProc/EEDI_East") && BatchProc301.contains("BatchProc/EEDI_West") ) {
			flag = true;
		}else {
			flag = false;
		}
		/*
		String LogDetails300 = " - No ERROR Found";
		String MailBody5 = MailBody300Log+LogDetails300+MailBodyNext+MailBodySlash+MailBodyNext;
		if(CebitLog300.contains("Error") || CebitLog300.contains("error") || CebitLog300.contains("Err")
				|| CebitLog300.contains("err") || CebitLog300.contains("Fail") || CebitLog300.contains("Failure") 
				|| CebitLog300.contains("Failed") || CebitLog300.contains("SQLRecoverableException") || CebitLog300.contains("NullPointerException")) {
			LogDetails300 = " - Error Found in the Cebit Log. Please check below.";
			MailBody5 = MailBody300Log+LogDetails300+MailBodyNext+MailBodyNext+CebitLog300+MailBodyNext+MailBodySlash+MailBodyNext;
			flag = false;
		}
		
		String LogDetails301 = " - No ERROR Found";
		String MailBody6 = MailBody301Log+LogDetails301+MailBodyNext+MailBodySlash+MailBodyNext;
		if(CebitLog301.contains("Error") || CebitLog301.contains("error") || CebitLog301.contains("Err")
				|| CebitLog301.contains("err") || CebitLog301.contains("Fail") || CebitLog301.contains("Failure") 
				|| CebitLog301.contains("Failed") || CebitLog301.contains("SQLRecoverableException") || CebitLog301.contains("NullPointerException")) {
			LogDetails301 = " - Error Found in the Cebit Log. Please check below.";
			MailBody6 = MailBody301Log+LogDetails301+MailBodyNext+MailBodyNext+CebitLog301+MailBodyNext+MailBodySlash+MailBodyNext;
			flag = false;
		}
		*/
		if(flag == false) {
			subject = "Cebit Process Validation - Red";
		}
		//MailBody = MailBody1 + MailBody2 + MailBody3 + MailBody4 + MailBody5 + MailBody6;
		MailBody = MailBody1 + MailBody2 + MailBody3 + MailBody4 + MailBody5 + MailBody6;
		CebitProcessEmail email = new CebitProcessEmail();
		email.CebitProcessEmailSend(subject,MailBody);
	}

	private static String CebitProcessCheck(String user, String pwd, String host, String command) throws AddressException, JSchException, IOException {
		
		
		Session session = null;
		String result = "";
		try {
			JSch jSch = new JSch();
			session = jSch.getSession(user, host, 22);
			session.setPassword(pwd);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			List<String> lines = new LinkedList<String>();
			lines = sendCommand(command,session);
			for(String z:lines) {
				result += z+"\n";
			}
		}finally {
			if(session != null) {
				session.disconnect();
			}
		}
		return result;
	}
	public static List<String> sendCommand(String command, Session session) throws JSchException, IOException{
		List<String> lines = new LinkedList<String>();
		ChannelExec channel = null;
		channel =(ChannelExec) session.openChannel("exec");
		InputStream in = channel.getInputStream();
		channel.setCommand(command);
		channel.setErrStream(System.err);
		channel.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while((line = reader.readLine()) != null) {
			lines.add(line);
		}
		channel.disconnect();
		return lines;

	}

}
