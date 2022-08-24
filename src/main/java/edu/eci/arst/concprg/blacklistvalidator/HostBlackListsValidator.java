/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.blacklistvalidator;

import edu.eci.arst.concprg.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N) throws InterruptedException {
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();

        LinkedList<SearchThread> listThread = new LinkedList<>();

        int ocurrencesCount=0;
        
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

        //System.out.prinseartln("AAAAA " + skds.getRegisteredServersCount() % N);

        int parts = skds.getRegisteredServersCount()/N;

        Object lock = new Object();

        int checkedInitial = 0;
        int checkedListsCount = parts;

        for (int i=0;i<N;i++) {
            //System.out.println(checkedInitial);
            //System.out.println(checkedListsCount);
            listThread.add(new SearchThread(ipaddress, checkedInitial, checkedListsCount, lock));
            checkedInitial = checkedListsCount;
            checkedListsCount = (i == N - 2) ?  (checkedListsCount + parts) + skds.getRegisteredServersCount() % N :  checkedListsCount + parts;

        }for (SearchThread thread:listThread) {
            thread.start();
        }
        for (SearchThread thread:listThread) {
            try {
                thread.join();
                ocurrencesCount += thread.getOcurrencesCount();
                blackListOcurrences.add(thread.getOcurrencesCount());
                if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
                    skds.reportAsNotTrustworthy(ipaddress);
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (ocurrencesCount < BLACK_LIST_ALARM_COUNT){
            for (SearchThread thread:listThread) {
                thread.killThread();
            }
            skds.reportAsTrustworthy(ipaddress);
        }
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
