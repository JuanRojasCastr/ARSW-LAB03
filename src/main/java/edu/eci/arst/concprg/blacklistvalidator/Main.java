/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator hblv=new HostBlackListsValidator();
        List<Integer> blackListOcurrences=hblv.checkHost("202.24.34.55", 32);

//        SearchThread thread = new SearchThread("200.24.34.55", 0, 1000);

//        thread.run();

//        System.out.println(thread.getOcurrencesCount());

        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
        
    }
    
}
