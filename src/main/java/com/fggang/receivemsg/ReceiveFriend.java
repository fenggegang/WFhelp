package com.fggang.receivemsg;

import com.fggang.tools.IO;

import java.io.IOException;

public class ReceiveFriend {
    public boolean updateMenu(String msg) {
        try {
            if( IO.setText("C:\\Users\\Administrator\\Desktop\\mcl_1\\plugins\\菜单.txt" , msg.substring(5))) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
