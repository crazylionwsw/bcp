package com.fuze.bcp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class Sol_LoanContract2 extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6108aa8061001e6000396000f3006060604052600436106100565763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663504006ca811461005b578063c1ff24461461018f578063f89dcbfd14610246575b600080fd5b341561006657600080fd5b610071600435610259565b60405173ffffffffffffffffffffffffffffffffffffffff88168152604081018690526080810184905260a0810183905260c0810182905260e060208201818152906060830190830189818151815260200191508051906020019080838360005b838110156100ea5780820151838201526020016100d2565b50505050905090810190601f1680156101175780820380516001836020036101000a031916815260200191505b50838103825287818151815260200191508051906020019080838360005b8381101561014d578082015183820152602001610135565b50505050905090810190601f16801561017a5780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b341561019a57600080fd5b61023460046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094968635969095506040808201955060209182013587018083019550359350839250601f8301829004820290910190519081016040528181529291906020840183838082843750949650508435946020810135945060400135925061041f915050565b60405190815260200160405180910390f35b341561025157600080fd5b61023461069d565b60006102636106a4565b600061026d6106a4565b60008060008060008981548110151561028257fe5b906000526020600020906007020190508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169750806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103515780601f1061032657610100808354040283529160200191610351565b820191906000526020600020905b81548152906001019060200180831161033457829003601f168201915b5050505050965080600201549550806003018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103f75780601f106103cc576101008083540402835291602001916103f7565b820191906000526020600020905b8154815290600101906020018083116103da57829003601f168201915b5050505050945080600401549350806005015492508060060154915050919395979092949650565b60006104296106b6565b60006001861161043857600080fd5b60e0604051908101604052803373ffffffffffffffffffffffffffffffffffffffff1681526020018a81526020018960001916815260200188815260200187815260200186815260200185815250915060016000805480600101828161049e9190610700565b600092835260209092208591600702018151815473ffffffffffffffffffffffffffffffffffffffff191673ffffffffffffffffffffffffffffffffffffffff919091161781556020820151816001019080516104ff929160200190610731565b5060408201516002820155606082015181600301908051610524929160200190610731565b506080820151816004015560a0820151816005015560c08201518160060155505003905081606001516040518082805190602001908083835b6020831061057c5780518252601f19909201916020918201910161055d565b6001836020036101000a0380198251168184511617909252505050919091019250604091505051908190039020825173ffffffffffffffffffffffffffffffffffffffff167f7f23f454cbc6e0a449c223792612e3046f0fcdb8b2613c5d47176d27b5ba7e568460200151856040015186608001518760a001518860c001516040516020810185905260408101849052606081018390526080810182905260a08082528190810187818151815260200191508051906020019080838360005b8381101561065357808201518382015260200161063b565b50505050905090810190601f1680156106805780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a398975050505050505050565b6000545b90565b60206040519081016040526000815290565b60e060405190810160405260008152602081016106d16106a4565b8152600060208201526040016106e56106a4565b81526020016000815260200160008152602001600081525090565b81548183558181151161072c5760070281600702836000526020600020918201910161072c91906107af565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061077257805160ff191683800117855561079f565b8280016001018555821561079f579182015b8281111561079f578251825591602001919060010190610784565b506107ab92915061081d565b5090565b6106a191905b808211156107ab57805473ffffffffffffffffffffffffffffffffffffffff1916815560006107e76001830182610837565b60028201600090556003820160006107ff9190610837565b506000600482018190556005820181905560068201556007016107b5565b6106a191905b808211156107ab5760008155600101610823565b50805460018160011615610100020316600290046000825580601f1061085d575061087b565b601f01602090049060005260206000209081019061087b919061081d565b505600a165627a7a723058206a55f4a8bdb978c5ea95657d17371558b3bedd98d97bd07e2efbed47ed855bc70029";

    protected Sol_LoanContract2(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Sol_LoanContract2(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<AddLoanEventResponse> getAddLoanEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("AddLoan", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<AddLoanEventResponse> responses = new ArrayList<AddLoanEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            AddLoanEventResponse typedResponse = new AddLoanEventResponse();
            typedResponse._sender = (Address) eventValues.getIndexedValues().get(0);
            typedResponse._identityHash = (Utf8String) eventValues.getIndexedValues().get(1);
            typedResponse._customerName = (Utf8String) eventValues.getNonIndexedValues().get(0);
            typedResponse._idNumber = (Bytes32) eventValues.getNonIndexedValues().get(1);
            typedResponse._amount = (Uint256) eventValues.getNonIndexedValues().get(2);
            typedResponse._loanDate = (Uint256) eventValues.getNonIndexedValues().get(3);
            typedResponse._mortgageDate = (Uint256) eventValues.getNonIndexedValues().get(4);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddLoanEventResponse> addLoanEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("AddLoan", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddLoanEventResponse>() {
            @Override
            public AddLoanEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                AddLoanEventResponse typedResponse = new AddLoanEventResponse();
                typedResponse._sender = (Address) eventValues.getIndexedValues().get(0);
                typedResponse._identityHash = (Utf8String) eventValues.getIndexedValues().get(1);
                typedResponse._customerName = (Utf8String) eventValues.getNonIndexedValues().get(0);
                typedResponse._idNumber = (Bytes32) eventValues.getNonIndexedValues().get(1);
                typedResponse._amount = (Uint256) eventValues.getNonIndexedValues().get(2);
                typedResponse._loanDate = (Uint256) eventValues.getNonIndexedValues().get(3);
                typedResponse._mortgageDate = (Uint256) eventValues.getNonIndexedValues().get(4);
                return typedResponse;
            }
        });
    }

    public RemoteCall<Tuple7<Address, Utf8String, Bytes32, Utf8String, Uint256, Uint256, Uint256>> getLoan(Uint256 _index) {
        final Function function = new Function("getLoan", 
                Arrays.<Type>asList(_index), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple7<Address, Utf8String, Bytes32, Utf8String, Uint256, Uint256, Uint256>>(
                new Callable<Tuple7<Address, Utf8String, Bytes32, Utf8String, Uint256, Uint256, Uint256>>() {
                    @Override
                    public Tuple7<Address, Utf8String, Bytes32, Utf8String, Uint256, Uint256, Uint256> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);;
                        return new Tuple7<Address, Utf8String, Bytes32, Utf8String, Uint256, Uint256, Uint256>(
                                (Address) results.get(0), 
                                (Utf8String) results.get(1), 
                                (Bytes32) results.get(2), 
                                (Utf8String) results.get(3), 
                                (Uint256) results.get(4), 
                                (Uint256) results.get(5), 
                                (Uint256) results.get(6));
                    }
                });
    }

    public RemoteCall<TransactionReceipt> addLoan(Utf8String _customerName, Bytes32 _idNumber, Utf8String _nameAndNumber, Uint256 _amount, Uint256 _loanDate, Uint256 _mortgageDate) {
        Function function = new Function(
                "addLoan", 
                Arrays.<Type>asList(_customerName, _idNumber, _nameAndNumber, _amount, _loanDate, _mortgageDate), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Uint256> getLoansLength() {
        Function function = new Function("getLoansLength", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public static RemoteCall<Sol_LoanContract2> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sol_LoanContract2.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Sol_LoanContract2> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sol_LoanContract2.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Sol_LoanContract2 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sol_LoanContract2(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Sol_LoanContract2 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sol_LoanContract2(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class AddLoanEventResponse {
        public Address _sender;

        public Utf8String _identityHash;

        public Utf8String _customerName;

        public Bytes32 _idNumber;

        public Uint256 _amount;

        public Uint256 _loanDate;

        public Uint256 _mortgageDate;
    }
}
