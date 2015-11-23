package myChatSvr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class mChatSer extends Thread {
	int count = 0;
	private Socket soc;
	String allmov[] = new String[109];
	String inc = "";

	int r_num[] = new int[109];

	private Vector<Socket> svec;

	public mChatSer(Socket sc, Vector<Socket> v) {

		soc = sc; // 세터
		svec = v;

		System.out.println("연결 : " + soc.getInetAddress()); // 연결된 ip주소를 출력

		int r, w;
		w = 0;
		boolean swit[] = new boolean[109];
		for (int i = 0; i < 108; i++)
			swit[i] = false;// 00000 00000

		while (w < 109) {
			r = (int) (Math.random() * 109);
			if (swit[r] == false) {
				swit[r] = true; // 00000 10000
				r_num[w] = r + 1; // 1~10

				w++;

			}

		}

		Choise Ch = new Choise();
		for (int q = 0; q < 109; q++) {
			Moviemenu Moviemenu = Ch.search(r_num[q]);
			allmov[q] = ("#$%@" + Moviemenu.getChr() + "@" + Moviemenu.getMovie() + "@" + Moviemenu.getQuest() + "@"
					+ Moviemenu.getHint1() + "@" + Moviemenu.getHint2() + "@" + Moviemenu.getHint3());

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {
		BufferedReader reader = null;// 버퍼
		PrintWriter sendout = null;// 출력

		if (count < 109) {
			count = 0;
		}
		try {

			// recv
			reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));// 버퍼에
																						// 소켓에
																						// 내용을
																						// 저장
			int Num = 1;

			String line = null;// 문자열 변수 line
			for (int h = 0; h < svec.size(); h++) {
				sendout = new PrintWriter(svec.get(h).getOutputStream(), true);// 벡터에
				sendout.println(allmov[count]);

			}

			sendout.flush();
			
			while (true) {

				line = reader.readLine();// 버퍼를 읽어와서 저장
				System.out.println("수신 메시지 :" + line);// 내용을 출력함

				if (line == null) // 버퍼에 내용이 없으면 멈춤== 접속 끊김
				{
					// vector 삭제
					svec.remove(soc);
					break;
				}

				// send
				// 자기 자신의 소켓을 제외한 다른 소켓에 전송
				// for,while을 사용

				String[] inan01 = line.split("@");

				System.out.println("-----------1-----------");
				System.out.println(line);
				System.out.println(allmov[count]);
				System.out.println("----------------------");

				String[] innan = allmov[count].split("@");

				if (inan01[0].equals("*&^")) {
					count += 1;
					for (int b = 0; b < svec.size(); b++) {
						sendout = new PrintWriter(svec.get(b).getOutputStream(), true);
						sendout.println(allmov[count]);

					}
					sendout.flush();
					
					System.out.println("----------timeout------------");
					System.out.println(line);
					System.out.println(allmov[count]);
					System.out.println("----------------------");
				} else if (innan[2].equals(inan01[1])) {
					count += 1;
					for (int b = 0; b < svec.size(); b++) {
						
						if (svec.get(b) != soc) {
							sendout = new PrintWriter(svec.get(b).getOutputStream(), true);
							sendout.println(line);}
						
						sendout = new PrintWriter(svec.get(b).getOutputStream(), true);
						sendout.println(allmov[count]);
				
					}
					
					sendout.flush();
				
					System.out.println("----------2------------");
					System.out.println(allmov[count]);
					System.out.println("----------------------");

				} else {
					for (int i = 0; i < svec.size(); i++) {
						if (svec.get(i) != soc) {
							sendout = new PrintWriter(svec.get(i).getOutputStream(), true);
							sendout.println(line);
							

						}
						sendout.flush();

					}
				}
			}
			Thread.sleep(10); // 스레드 10ms딜레이

		} catch (

		IOException e)

		{
			// System.out.println("예외1");
			// e.printStackTrace();
		} catch (

		InterruptedException e)

		{
			e.printStackTrace();
		} finally

		{
			try {
				if (reader != null)
					reader.close();// 버퍼에 내용이 있으면 리더 닫음
				if (soc != null) // 소켓에 내용이 있으면 소켓 닫음
				{
					soc.close();
				}
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}
	}

}
