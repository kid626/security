package com.bruce.security.util;

import org.junit.Test;

/**
 * @Copyright Copyright © 2021 fanzh . All rights reserved.
 * @Desc
 * @ProjectName security
 * @Date 2021/3/26 15:51
 * @Author fzh
 */
public class JwtUtilTest {

    @Test
    public void test(){
        long exp = System.currentTimeMillis() / 1000 + 10L;
        String encode = JwtUtil.encode("{\"exp\":1616828021,\"expired\":false,\"jti\":\"a4c5d57b-9167-426f-93c1-bc374b8e7aec\",\"roles\":[\"ROLE_USER\"],\"uid\":\"605d3b2ab5ef360001e38f8e\"}", exp);
        System.out.println(encode);
        String origin = JwtUtil.decode(encode);
        System.out.println(origin);
    }

}
