package com.matrupeeth.store.security;

import com.matrupeeth.store.exception.BadApiRequest;
import com.matrupeeth.store.services.Impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private  JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Authentication
        String requestHeader = request.getHeader("Authorization");
        //Bearer Authentication
        logger.info("Header : {}",requestHeader);
        String username=null;
        String token=null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer"))
        {
            token = requestHeader.substring(7);
            try {
                username= this.jwtHelper.getUsernameFromToken(token);

            }catch (Exception e) {
//                e.printStackTrace();
                throw new BadApiRequest("Bad authorization header!!");
            }

        }
        else
        {
            logger.info("Invalid header value : {} !!",requestHeader);
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            //feacth user d
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if(validateToken)
            {
                UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else
            {
                logger.info("validate failed!!!");
            }
        }
        filterChain.doFilter(request,response);

    }
}
