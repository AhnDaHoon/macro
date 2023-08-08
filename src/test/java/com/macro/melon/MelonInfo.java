package com.macro.melon;

import com.macro.melon.config.LoginTypeEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MelonInfo {
    private String id;

    private String pwd;

    private String url;

    private LoginTypeEnum loginType;

}
