package com.wuwei.auth.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wuwei.auth.entity.LoginVO;
import com.wuwei.auth.utils.AuthUtils;
import com.example.common.cache.component.RedisUUID;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.core.constants.ServiceNameConstants;
import com.example.common.core.entity.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * @author czx
 * @title: LoginController
 * @projectName ms
 * @description: TODO
 * @date 2019/8/1911:15
 */
@Slf4j
@RestController
@RequestMapping("/authority")
@AllArgsConstructor
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUUID redisUUID;

    /**
     * 认证页面
     * 具体博客 https://blog.csdn.net/ahcr1026212/article/details/84813955
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView require() {
        return new ModelAndView("ftl/login");
    }

    /**
     * 统一获取token
     * 具体博客 https://blog.csdn.net/m0_37834471/article/details/83213002
     * @return R
     */
    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public R token(LoginVO vo){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if(StrUtil.isBlank(vo.getGrant_type())){
            return R.error("认证模式grant_type为空");
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(AuthUtils.BASIC_)) {
            return R.error("请求头中client信息为空");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, header);
        httpHeaders.set(SecurityConstants.SECRET_KEY, redisUUID.create(SecurityConstants.SECRET_KEY));

        //授权请求信息
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        String uri = AuthUtils.AUTH_URL;
        switch (vo.getGrant_type()){
            case AuthUtils.PASSWORD:
                map.put("username", Collections.singletonList(vo.getUsername()));
                map.put("password", Collections.singletonList(vo.getPassword()));
                map.put("grant_type", Collections.singletonList(vo.getGrant_type()));
                map.put("scope", Collections.singletonList(vo.getScope()));
                break;
            case AuthUtils.CLIENT_CREDENTIALS:
                map.put("grant_type", Collections.singletonList(vo.getGrant_type()));
                map.put("scope", Collections.singletonList(vo.getScope()));
                break;
            case AuthUtils.SOCIAL:
                uri = AuthUtils.SOCIAL_AUTH_URL;
                map.put("grant_type", Collections.singletonList(vo.getGrant_type()));
                map.put("social", Collections.singletonList(vo.getAttr()));
                break;
            case AuthUtils.AUTHORIZATION_CODE:
                map.put("grant_type", Collections.singletonList(vo.getGrant_type()));
                map.put("code", Collections.singletonList(vo.getCode()));
                map.put("redirect_uri", Collections.singletonList(vo.getAttr()));
                break;
            case AuthUtils.REFRESH_TOKEN:
                break;
            default:
                return R.error("无效认证模式"+vo.getGrant_type());
        }
        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);
        ResponseEntity<OAuth2AccessToken> body = null;
        try{
            body = restTemplate.exchange(
                    "http://"+ ServiceNameConstants.MS_AUTH_SERVICE + uri,
                    HttpMethod.POST,
                    httpEntity, OAuth2AccessToken.class);
        }catch (HttpClientErrorException e){
            JSONObject jsonObject = JSONUtil.parseObj(e.getResponseBodyAsString());
            return R.error(jsonObject.getInt("code"),jsonObject.getStr("msg"));
        }
        if(body != null && body.getStatusCode() == HttpStatus.OK){
            return R.ok().setData(body.getBody());
        }else {
            return R.error("认证错误");
        }
    }
}
