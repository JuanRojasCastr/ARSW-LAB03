package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class SearchThread extends Thread {

    String ipaddress;

    int from;

    int to;

    public SearchThread(String ipaddress, int from, int to) {
        this.ipaddress = ipaddress;
        this.from = from;
        this.to = to;
    }

    int ocurrencesCount = 0;

    public void run() {

        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

        int checkedListsCount = 0;

        for (int i = from; i < to; i++){
            checkedListsCount++;


            if (skds.isInBlackListServer(i, ipaddress)){

                this.ocurrencesCount++;
            }
        }
    }

    public int getOcurrencesCount() {
        return ocurrencesCount;
    }

    public static void main() {
    }
}
