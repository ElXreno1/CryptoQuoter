# cryptoquoter
The app gets tickers from Binance and Polonex exhanges using a info.bitrich.xchange-stream library, makes synthetic instruments and saves to MySQL DB.

Cryptoquoter requires settings-cryptoquoter.yml file, e.g.:  
<pre>db:  
  url: localhost  
  database: quotes  
  user: root  
  password: 4OtZyb3s0pmqBTEne1tjp/JiUD3/Nh66  
flush_period_s: 60  
instruments:  
  - name: BTCUSD  
    instrument: BTC/USDT  
  - name: ETHUSD  
    instrument: ETH/USDT  
  - name: ETHUSD-synth1  
    instrument: ETH/USD  
    depends:  
      - ETH/BTC  
      - BTC/USD  
  - name: ETHUSD-synth2  
    instrument: ETH/USD  
    depends:  
      - ETH/BCH  
      - USD/BCH  
</pre>
Password must be encrypted. You can ecrypted data for DB password using cryptoquoter with -p key.
  
First run makes table quotes in DB if there is not exist.  

flush_period_s set period to flush data from memory buffer to DB.

Usage:  
- print help:
<pre>java -jar cryptoquoter.jar -h</pre>   
- get encrypted password for *.yml file:
<pre>java -jar cryptoquoter.jar -p</pre>
- streaming
<pre>java -jar cryptoquoter.jar
 
  