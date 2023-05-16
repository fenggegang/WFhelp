package com.fggang.sendmsg;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;

public class SendGroup {
    public static void SendMsg(Group e , String s) {
        e.sendMessage(s);
    }

    public static void SendMsg(Group e , Message s) {
        e.sendMessage(s);
    }

}
