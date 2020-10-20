package MusafirServer;

import java.io.*;
import java.net.*;
import java.sql.*;

import Classes.*;
public class HandleClient implements Runnable{
    private Conn c1=new Conn();
    private Socket socket;
    public HandleClient(Socket socket){
        this.socket=socket;
    }

    private String Login(LoginInfo loginInfo){
        try{
        String query="SELECT * FROM `user_info` WHERE email='"+loginInfo.getUsername()+"' AND password='"+String.valueOf(loginInfo.getPassword())+"'";
        
            ResultSet rs=c1.s.executeQuery(query);
            if(rs.next())
            {
                return rs.getString("name");
            }
        query="SELECT * FROM `user_info` WHERE phone='"+loginInfo.getUsername()+"' AND password='"+String.valueOf(loginInfo.getPassword())+"'";
            rs=c1.s.executeQuery(query);
            if(rs.next())
            {
                return rs.getString("name");
            }
        }catch(Exception e){
            e.printStackTrace();
                
        }
        return "";
    }
    private Boolean Signup(UserInfo userInfo){
        
        String d=userInfo.getMm()+"-"+userInfo.getDd()+"-"+userInfo.getYy();
        String query="INSERT INTO `user_info` ( `name`, `dob`, `gender`, `email`, `phone`, `password`) VALUES ( '"+userInfo.getName()+"', STR_TO_DATE('"+d+"','%m-%d-%Y'), '"+userInfo.getGender()+"', '"+userInfo.getEmail()+"', '"+userInfo.getPhone()+"', '"+String.valueOf(userInfo.getPassword())+"')";
        try{
        c1.s.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
            
        }

        return true;
    }
    @Override
    public void run() {
        while(true){
            try{
                
                ObjectInputStream oi =new ObjectInputStream(socket.getInputStream());
                
                ObjectOutputStream os =new ObjectOutputStream(socket.getOutputStream());
                int choice =(int)oi.readInt();
                switch(choice){
                    case 1:
                    LoginInfo loginInfo=(LoginInfo)oi.readObject();
                    String s= Login(loginInfo);
                    os.writeUTF(s);
                    
                    os.flush();
                    break;

                    case 2:
                    UserInfo userInfo=(UserInfo)oi.readObject();
                    Boolean b =Signup(userInfo);
                    os.writeBoolean(b);
                    os.flush();
                    break;

                }
                
                
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
        
    }
}
