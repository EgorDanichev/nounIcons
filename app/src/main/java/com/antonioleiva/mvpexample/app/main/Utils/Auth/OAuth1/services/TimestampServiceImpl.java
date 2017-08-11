package com.antonioleiva.mvpexample.app.main.Utils.Auth.OAuth1.services;

import java.util.Random;

/**
 * This class generate timestamp and nonce
 */
public class TimestampServiceImpl implements TimestampService
{
    private Timer timer;

    /**
     * Default constructor.
     */
    public TimestampServiceImpl()
    {
        timer = new Timer();
    }

    /**
     * {@inheritDoc}
     */
    public String getNonce()
    {
        return getRandomString();
    }

    /**
     * {@inheritDoc}
     */
    public String getTimestampInSeconds()
    {
        return String.valueOf(getTs());
    }

    private Long getTs()
    {
        return timer.getMilis() / 1000;
    }

    void setTimer(Timer timer)
    {
        this.timer = timer;
    }

    /**
     * Inner class that uses {@link System} for generating the timestamps.
     *
     * @author Pablo Fernandez
     */
    static class Timer
    {
        private final Random rand = new Random();
        Long getMilis()
        {
            return System.currentTimeMillis();
        }

        Integer getRandomInteger()
        {
            return rand.nextInt();
        }
    }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    private static String getRandomString()
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(16);
        for(int i=0;i<16;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}