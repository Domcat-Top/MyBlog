package com.tom.pojo;


import lombok.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Foreignkey implements Serializable {

    public int id;
    public String label;
    public Date create_time;
    public Date update_time;

    public Foreignkey(int id, String label) {
        this.id = id;
        this.label = label;
    }


    public String formatCreateTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(create_time);
    }
    public String formatUpdateTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(update_time);
    }













}
