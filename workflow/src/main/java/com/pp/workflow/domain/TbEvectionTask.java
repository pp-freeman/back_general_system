package com.pp.workflow.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class TbEvectionTask implements Serializable {
    TbEvection tbEvection;
    String taskId;
}
