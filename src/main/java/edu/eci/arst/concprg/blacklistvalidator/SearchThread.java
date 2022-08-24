package edu.eci.arst.concprg.blacklistvalidator;

import edu.eci.arst.concprg.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class SearchThread extends Thread {

    String ipaddress;

    int from;

    int to;

    private final Object lock;

    public SearchThread(String ipaddress, int from, int to, Object lock) {
        this.ipaddress = ipaddress;
        this.from = from;
        this.to = to;
        this.lock = lock;
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

    public void killThread() throws InterruptedException {
        synchronized (lock) {
            lock.wait();
        }
    }

    public static void main() {
    }
}
