package com.dmsql5303.shop.myblog.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ArletMessage {

    String message = "";
    String href = "";

    public ArletMessage(String message, String href){
        this.message = message;
        this.href = href;
    }

}
