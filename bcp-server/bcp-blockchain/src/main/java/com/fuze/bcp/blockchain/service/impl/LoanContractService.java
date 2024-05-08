package com.fuze.bcp.blockchain.service.impl;

import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.blockchain.contract.Sol_LoanContract;
import com.fuze.bcp.blockchain.domain.LoanContract;
import com.fuze.bcp.blockchain.service.ILoanContractService;
import com.fuze.bcp.blockchain.starter.config.Web3jConfig;
import com.fuze.bcp.utils.EncryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Lily on 2018/4/20.
 */
@Service
public class LoanContractService implements ILoanContractService {

    @Autowired
    Web3jConfig web3jConfig;
    /**
     * 签约通过后将交易信息上链
     * @param map
     * @return
     */
    @Override
    public ResultBean<LoanContract> actMonitoringOrder(Map map) {
        if(map != null && map.size() > 0){
            LoanContract loanContract = new LoanContract();
            String hash = (String)map.get("Hash");
            if(hash != null){
                loanContract.setHASH(hash);
            }
            String customerName = (String)map.get("customerName");
            if(customerName != null){
                loanContract.setCustomerName(customerName);
            }
            String identifyNo = (String)map.get("identifyNo");
            if(identifyNo != null){
                loanContract.setIdentifyNo(identifyNo);
            }
            Double creditAmount = (Double) map.get("creditAmount");
            if(creditAmount != null){
                loanContract.setCreditAmount(creditAmount);
            }
            String loanDate = (String)map.get("loanDate");
            if(loanDate != null){
                loanContract.setLoanDate(loanDate);
            }
            return this.actAddLoanTransaction(loanContract);
        }
        return null ;
    }


    /**
     * 抵押完成后将交易信息存入链中
     * @param map
     * @return
     */
    @Override
    public ResultBean<LoanContract> actMonitoringDMVPledge(Map map) {
        if(map != null && map.size() > 0){
            LoanContract loanContract = new LoanContract();
            String hash = (String)map.get("Hash");
            if(hash != null){
                loanContract.setHASH(hash);
            }
            String loanDate = (String)map.get("loanDate");
            if(loanDate != null){
                loanContract.setLoanDate(loanDate);
            }
            String dmvpledgeDate = (String)map.get("dmvpledgeDate");
            if(dmvpledgeDate != null){
                loanContract.setDmvpledgeDate(dmvpledgeDate);
            }
            return this.actUpdateLoanTransaction(loanContract);
        }
        return null ;
    }

    /**
     * 校验和数据处理
     * @param loanContract
     * @return
     */
    public LoanContract verifyLoanContractBean(LoanContract loanContract){
        if(loanContract != null){
            if(loanContract.getCustomerName() != null){
                //将姓名中间的内容替换为星号
                if(loanContract.getCustomerName().length() == 1){
                    loanContract.setCustomerName(loanContract.getCustomerName());
                } else if(loanContract.getCustomerName().length() == 2){
                    loanContract.setCustomerName(loanContract.getCustomerName().replaceFirst(loanContract.getCustomerName().substring(1),"*"));
                }else if (loanContract.getCustomerName().length() > 2) {
                    String name = loanContract.getCustomerName().replaceFirst(loanContract.getCustomerName().substring(1,loanContract.getCustomerName().length()-1) ,"*");
                    loanContract.setCustomerName(name);
                }
            }
            //将身份证中间8位替换星号
            if(loanContract.getIdentifyNo() != null){
                char[] identifyNo = loanContract.getIdentifyNo().toCharArray();
                for(int i=0; i<identifyNo.length;i++){
                    if(i>5 && i<14){
                        identifyNo[i] = '*';
                    }
                }
                loanContract.setIdentifyNo(String.valueOf(identifyNo));
            }
            System.out.println("接收到的hash值为"+loanContract.getHASH());
        }
        return loanContract;
    }

    @Override
    public ResultBean<LoanContract> actAddLoanTransaction(LoanContract loanContract) {
        //校验和处理
        loanContract = this.verifyLoanContractBean(loanContract);

        //File file = new File("E:/OtherData/Dataworksapce/geth/privatechain/data0/keystore/UTC--2018-03-19T09-45-49.541403700Z--d61e555371bbff704eded4a9ede69e0b37e0ab92");
        //String password = "123456";
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(web3jConfig.getPassword(),web3jConfig.getFile());
            // 填入刚才部署后打印出来的合约地址
            //String address = "0xc97dc217f1343f3f887be5a37e901463f0dfa76b";

            Sol_LoanContract contract = Sol_LoanContract.load(
                    web3jConfig.getAddress(),
                    web3jConfig.getClient(),
                    credentials,
                    BigInteger.valueOf(200000),
                    BigInteger.valueOf(20000000));
            //查看余额
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long loanDate = 0;
            long dmvpledgeDate = 0;
            if(loanContract.getLoanDate() != null){
                loanDate = sf.parse(loanContract.getLoanDate()).getTime();
            }
            if(loanContract.getDmvpledgeDate() != null){
                dmvpledgeDate = sf.parse(loanContract.getDmvpledgeDate()).getTime();
            }
            //四舍五入把double转化int整型，0.5进一，小于0.5不进一
            BigDecimal bd=new BigDecimal(loanContract.getCreditAmount()).setScale(0, BigDecimal.ROUND_HALF_UP);
            TransactionReceipt loan = contract.addLoan(new Utf8String(loanContract.getCustomerName()),
                    new Bytes32(Arrays.copyOfRange(loanContract.getIdentifyNo().getBytes(), 0, 32)),
                    new Bytes32(getbytes(loanContract.getHASH())),
                    new Uint256(Integer.parseInt(bd.toString())),
                    new Uint256(loanDate),
                    new Uint256(dmvpledgeDate)).send();
            System.out.println(loan);
            this.actGetLoanTransaction(loanContract.getHASH(),loanContract.getLoanDate());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResultBean<LoanContract> actUpdateLoanTransaction(LoanContract loanContract) {
        //校验和处理
        //loanContract = this.verifyLoanContractBean(loanContract);

        //File file = new File("E:/OtherData/Dataworksapce/geth/privatechain/data0/keystore/UTC--2018-03-19T09-45-49.541403700Z--d61e555371bbff704eded4a9ede69e0b37e0ab92");
        //String password = "123456";
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(web3jConfig.getPassword(),web3jConfig.getFile());
            // 填入刚才部署后打印出来的合约地址
            //String address = "0xc97dc217f1343f3f887be5a37e901463f0dfa76b";

            Sol_LoanContract contract = Sol_LoanContract.load(
                    web3jConfig.getAddress(),
                    web3jConfig.getClient(),
                    credentials,
                    BigInteger.valueOf(200000),
                    BigInteger.valueOf(20000000));
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long loanDate = 0;
            long dmvpledgeDate = 0;
            if(loanContract.getLoanDate() != null){
                loanDate = sf.parse(loanContract.getLoanDate()).getTime();
            }
            if(loanContract.getDmvpledgeDate() != null){
                dmvpledgeDate = sf.parse(loanContract.getDmvpledgeDate()).getTime();
            }

            TransactionReceipt send = contract.updateLoan(new Bytes32(getbytes(loanContract.getHASH())), new Uint256(loanDate), new Uint256(dmvpledgeDate)).send();
            System.out.println(send);
            this.actGetLoanTransaction(loanContract.getHASH(),loanContract.getLoanDate());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public ResultBean<List<LoanContract>> actGetLoanTransaction(String hash,String date) {
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
            //查看余额
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256> send = loanContract.getLoanByHash(new Bytes32(hash.getBytes()), new Uint256(sf.parse(date).getTime())).send();
                System.out.println("**************************************************");
                System.out.println("交易信息：姓名="+send.getValue2()+"\n身份证号="+new String(send.getValue3().getValue())+"\nHASH值="+new String(send.getValue4().getValue())+"\n贷款金额="+send.getValue5().getValue()+"\n放款日期="+ send.getValue6().getValue() +"\n抵押日期="+send.getValue7().getValue());
                System.out.println(send);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * String转byte
     * @param data
     * @return
     */
    public static byte[] getbytes(String data) {

        byte[] bytes = data.getBytes();
        //因为byte默认8个，byte32 是32个，要扩容，这一步相当克隆，从一个byte拿到另一个里面，0是开始的索引，32是结束的
        byte[] longByte = Arrays.copyOfRange(bytes, 0, 32);
        return longByte;
    }
}
