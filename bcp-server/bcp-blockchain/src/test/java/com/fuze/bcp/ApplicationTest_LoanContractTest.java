package com.fuze.bcp;

import com.alibaba.fastjson.JSON;
import com.fuze.bcp.blockchain.BlockCHainBootStarter;
import com.fuze.bcp.blockchain.contract.Sol_LoanContract;
import com.fuze.bcp.blockchain.domain.LoanContract;
import com.fuze.bcp.blockchain.service.ILoanContractService;
import com.fuze.bcp.blockchain.starter.config.Web3jConfig;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple7;

import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by Lily on 2018/3/29.
 */
//@RunWith(SpringRunner.class)
@SpringBootTest(classes=BlockCHainBootStarter.class)
@FixMethodOrder(MethodSorters.JVM)
public class ApplicationTest_LoanContractTest {

    @Autowired
    private ILoanContractService iLoanContractService;

    //private static Web3j web3j = Web3jConfig.getClient();
    @Autowired
    Web3jConfig web3jConfig;

    //@Test
    public void test1(){
        /*System.out.println("address   " + web3jConfig.getAddress());
        System.out.println(web3jConfig.getFile());
        System.out.println(web3jConfig.getPassword());*/
        LoanContract loan = new LoanContract();
        loan.setHASH("d96c94d297625a1317bcb4c78738c172");
        loan.setLoanDate("2018-05-02 13:10:35");
        loan.setDmvpledgeDate("2018-05-02 16:37:00");
        iLoanContractService.actUpdateLoanTransaction(loan);
    }
    /**
     * 部署
     */
    //@Test
    public void deployment(){
        // 部署的时候需要用到该账户的 gas，务必保证该账户余额充足
        // Credentials credentials ;
        try {
            File file = new File("E:/OtherData/Dataworksapce/geth/privatechain/data0/keystore/UTC--2018-03-19T09-45-49.541403700Z--d61e555371bbff704eded4a9ede69e0b37e0ab92");
            String password = "123456";
            Credentials credentials = WalletUtils.loadCredentials(password,file);
            // 部署合约
            Sol_LoanContract loanContract
                    = Sol_LoanContract.deploy(web3jConfig.getClient(), credentials, BigInteger.valueOf(200000), BigInteger.valueOf(20000000)).send();
            // 部署完成后打印合约地址
            System.out.println("合约地址："+loanContract.getContractAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证合约是否可用
     */
    //@Test
    public void verify(){
        //File file = new File("E:/OtherData/Dataworksapce/geth/privatechain/data0/keystore/UTC--2018-03-19T09-45-49.541403700Z--d61e555371bbff704eded4a9ede69e0b37e0ab92");
        //String password = "123456";
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(web3jConfig.getPassword(),web3jConfig.getFile());
            // 填入刚才部署后打印出来的合约地址
            //String address = "0xc97dc217f1343f3f887be5a37e901463f0dfa76b";

            Sol_LoanContract loanContract = Sol_LoanContract.load(
                    web3jConfig.getAddress(),
                    web3jConfig.getClient(),
                    credentials,
                    BigInteger.valueOf(200000),
                    BigInteger.valueOf(20000000));
            System.out.println(loanContract.isValid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用智能合约
     */
    //@Test
    public void getBalance(){
        //File file = new File("E:/OtherData/Dataworksapce/geth/privatechain/data0/keystore/UTC--2018-03-19T09-45-49.541403700Z--d61e555371bbff704eded4a9ede69e0b37e0ab92");
        //String password = "123456";
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(web3jConfig.getPassword(),web3jConfig.getFile());
            // 填入刚才部署后打印出来的合约地址0x8755eba978085c8734a3d4e7bc674bc472b294cb
            //String address = "0xc97dc217f1343f3f887be5a37e901463f0dfa76b";

            Sol_LoanContract loanContract = Sol_LoanContract.load(
                    web3jConfig.getAddress(),
                    web3jConfig.getClient(),
                    credentials,
                    BigInteger.valueOf(200000),
                    BigInteger.valueOf(20000000));
            //查看余额
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long creditTime = sf.parse("2018-05-02 17:38:50").getTime();
            //long dmvpTime = sf.parse("2018-05-09 15:20:00").getTime();
            long dmvpTime = 0;
            String a = "4127********1076523";
            //String hash = EncryUtil.MD5("岩棉412725199411076523");
            //TransactionReceipt loan = loanContract.addLoan(new Utf8String("岩*"),new Bytes32(getStringytes(a)), new Bytes32(hash.getBytes()), new Uint256(66666), new Uint256(creditTime), new Uint256(dmvpTime)).send();
            //Uint256 send = loanContract.getLoansLength().send();
            //Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256> send = loanContract.getLoan(new Uint256(0)).send();
            //System.out.println(loan);

            //查询账户
           int count = loanContract.getLoansLength().send().getValue().intValue();
            if(count > 0){
                System.out.println("交易数量："+count);
                for (int i = 0; i < count ; i++) {
                    Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256> send = loanContract.getLoan(new Uint256(i)).send();
                    String loandate = null;
                    BigInteger num = new BigInteger("0");
                    if(!send.getValue6().getValue() .equals(num)){
                        loandate = sf.format(send.getValue6().getValue());
                    }
                    String dmvpledgedate = null;
                    if(!send.getValue7().getValue() .equals(num)){
                        dmvpledgedate = sf.format(send.getValue7().getValue());
                    }
                    System.out.println("第【"+i+"】条交易信息：姓名="+send.getValue2()+"\n身份证号="+getBytesString(send.getValue3())+"\nHASH值="+new String(send.getValue4().getValue())+"\n贷款金额="+send.getValue5().getValue()+"\n放款日期="+ loandate +"\n抵押日期="+dmvpledgedate);
                }

                //根据hash和刷卡日期查询
                String str = "9f4be857c31a0e8b24b2f131d3db59dd";
                String date = "2018-03-29 19:20:00";
                String now = "2018-05-02 15:35:00";

                /*Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256> send = loanContract.getLoanByHash(new Bytes32(str.getBytes()), new Uint256(sf.parse(date).getTime())).send();
                System.out.println("**************************************************");
                System.out.println("交易信息：姓名="+send.getValue2()+"\n身份证号="+getBytesString(send.getValue3())+"\nHASH值="+new String(send.getValue4().getValue())+"\n贷款金额="+send.getValue5().getValue()+"\n放款日期="+ send.getValue6().getValue() +"\n抵押日期="+send.getValue7().getValue());
                System.out.println(send);

                //根据hash和刷卡日期更新抵押日期
                TransactionReceipt send2 = loanContract.updateLoan(new Bytes32(str.getBytes()), new Uint256(sf.parse(date).getTime()), new Uint256(sf.parse(now).getTime())).send();
                System.out.println("**************************************************");
                System.out.println(send2);*/

            }else{
                System.out.println("交易信息没有数据");
            }


            //转账
            /*TransactionReceipt send = compute_sol_compute.issue(new Address("0xd61e555371bbff704eded4a9ede69e0b37e0ab92"), new Uint256(100)).send();
            System.out.print(send);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*long a = 123456;
        //将long转成byte数组
        byte[] longBytes = getLongBytes(a);
        System.out.println("byte  " + JSON.toJSON(longBytes));
        //
        Bytes32 bytes32 =  new Bytes32(longBytes);
        System.out.println("byte32 " + JSON.toJSONString(bytes32));
        //
        long byteslong = getByteslong(bytes32);
        System.out.println("long32  " + JSON.toJSON(byteslong));*/
        //HASH值={"typeAsString":"bytes32","value":"3H9PxTrmuisMivU3RP6blV+LiFT8ugTRlSzQrnRFGns="}
        Utf8String hash = new Utf8String("王莉莉123456******789");
        int hashCode =  hash.hashCode();
        System.out.println("hashCode  "+hashCode);

        byte[] longBytes = getLongBytes(hashCode);
        System.out.println("hashCodeByte " + JSON.toJSONString(longBytes));

        Bytes32 hashCodeBytes32 = new Bytes32(longBytes);
        System.out.println("hashCodeBytes32  " + JSON.toJSONString(hashCodeBytes32));
    }

    /**
     * long转byte
     * @param data
     * @return
     */
    public static byte[] getLongBytes(long data) {
        //创建一个容量为8字节的ByteBuffe
        ByteBuffer buffer = ByteBuffer.allocate(8);
      //  buffer.order(ByteOrder.BIG_ENDIAN);
        //将数据存入缓冲区，0是第一个索引，从0开始存，这是个long装byte的核心方法
        buffer.putLong(0,data);
        //buff转byte
        byte[] bytes = buffer.array();
        //因为byte默认8个，byte32 是32个，要扩容，这一步相当克隆，从一个byte拿到另一个里面，0是开始的索引，32是结束的
        byte[] longByte = Arrays.copyOfRange(bytes, 0, 32);
        //zi ji kan
        return longByte;
    }

    /**
     * Byte32转long
     * @param data
     * @return
     */
    public static long getByteslong(Bytes32 data) {
        // 1 byte = 8 bit
        // 1 byte = 1字节
        //因为把long转成了byte 8个字节 又转换成32的，这里先还原成8个字节格式
        //Bytes32还必须换成byte
        byte[] bytes = Arrays.copyOfRange(data.getValue(),0,8);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        //默认走的是8个字节 = 64 bit
        buffer.put(bytes,0,bytes.length);
        //将当前缓冲区的写模式转换成读模式
        buffer.flip();
        long aLong = buffer.getLong();
        return aLong;
    }

    /**
     * String转byte
     * @param data
     * @return
     */
    public static byte[] getStringytes(String data) {

        byte[] bytes = data.getBytes();
        //因为byte默认8个，byte32 是32个，要扩容，这一步相当克隆，从一个byte拿到另一个里面，0是开始的索引，32是结束的
        byte[] longByte = Arrays.copyOfRange(bytes, 0, 32);
        return longByte;
    }

    /**
     * Byte32转String
     * @param data
     * @return
     */
    public static String getBytesString(Bytes32 data) {
        String str = new String(data.getValue());
        return str;
    }


    public void stringutilTest(){
        String realname =  "欧阳若兰欧辰";
        String mobile = "410521199632521478";
        String realname1 =null;
        //char[] r =  realname.toCharArray();
        char[] m =  mobile.toCharArray();
        if(realname.length() ==1){
            realname =  realname;
        }
        if(realname.length() == 2){
            realname =  realname.replaceFirst(realname.substring(1),"*");
        }
        if (realname.length() > 2) {
            realname =  realname.replaceFirst(realname.substring(1,realname.length()-1) ,"*");
        }
        for(int i=0; i<m.length;i++){
            if(i>5 && i<14){
                m[i] = '*';
            }
        }
        String mobile1 =  String.valueOf(m);
        System.out.println(realname);//程*员
        System.out.println(mobile1);//158****8888

    }

    /*@Test
    public void addLoanContract(){
        iLoanContractBizService.actMonitoringOrder("5ab31709be281b500479c9fb");
    }*/

}