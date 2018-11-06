package com.itstyle.domain.account.req;

import lombok.Data;

import java.util.List;

@Data
public class DeleteRoleIds {
    private List<Long> ids;
}
