package com.enclave.acton.ChatVertx;

import java.util.Scanner;

import io.vertx.core.Vertx;

public class SentMessageThread extends Thread {
    ChatService chatService;
    private String name;
    public SentMessageThread(ChatService chatService) {
        this.chatService = chatService;
    } 
    
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        name = scanner.nextLine();

        
        while(true){

            String sms = scanner.nextLine();
            
            chatService.sentMessage(name+": "+sms);
            
        }
    }
}
