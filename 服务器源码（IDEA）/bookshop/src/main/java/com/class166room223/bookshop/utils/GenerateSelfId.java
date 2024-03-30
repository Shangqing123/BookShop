package com.class166room223.bookshop.utils;
import java.util.UUID;
/**
 * ClassName:GenerateSelfId
 * Author:落魄山陈十一
 * Date:2019/6/9 21:15
 * Description:
 */
public class GenerateSelfId {


    public static String getSelfIdByUUId() {

        int machineId = 1;//最大支持1-9个集群机器部署

        int hashCodeV = UUID.randomUUID().toString().hashCode();

        if(hashCodeV < 0) {//有可能是负数

            hashCodeV = - hashCodeV;

        }

// 0 代表前面补充0

// 4 代表长度为4

// d 代表参数为正数型

        String str = "abdefghijmnqrt";

        int random = (int)(Math.random()*14);

        return str.charAt(random) + String.format("%08d", hashCodeV);

    }

}


