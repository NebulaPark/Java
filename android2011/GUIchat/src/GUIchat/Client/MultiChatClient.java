package GUIchat.Client;

import java.io.IOException;
import java.net.Socket;

public class MultiChatClient
{
    
    public static void main(String[] args)
    {
        
        Socket socket = null;
        ClientFrame cf;
        
        try
        {
            socket = new Socket("192.168.36.91", 19999);
            if(socket != null)
            {
                System.out.println("연결 성공!");
                
            }
            
            cf =new ClientFrame(socket);
            new ReadThread(socket, cf).start();;
        }
      
        catch (IOException e)
        {
            // TODO 자동 생성된 catch 블록
            e.printStackTrace();
        }
    }
    
}