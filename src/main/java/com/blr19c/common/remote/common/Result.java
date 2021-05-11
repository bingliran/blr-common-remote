package com.blr19c.common.remote.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 结果
 *
 * @author blr
 * @since 2021.4.16
 */
public class Result implements Serializable {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;

    private long currTime;
    private Object data;
    private int code;

    public Result() {
    }

    public Result(long currTime, Object data, int code) {
        this.currTime = currTime;
        this.data = data;
        this.code = code;
    }

    public long getCurrTime() {
        return currTime;
    }

    public void setCurrTime(long currTime) {
        this.currTime = currTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] toBytes() {
        try {
            return new ObjectMapper().writeValueAsString(this).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(toBytes());
    }

    public static Result success(Object data) {
        return new Result(System.currentTimeMillis(), data, SUCCESS);
    }

    public static Result fail(Object data) {
        return new Result(System.currentTimeMillis(), data, FAIL);
    }
}
