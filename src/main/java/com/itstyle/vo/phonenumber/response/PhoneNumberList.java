package com.itstyle.vo.phonenumber.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PhoneNumberList {
    private List<PhoneNumber> phoneNumberList = new ArrayList<>();
}
