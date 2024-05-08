package com.fuze.bcp.blockchain.contract;

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
public class Sol_LoanContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b610a8d8061001e6000396000f3006060604052600436106100775763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166330928664811461007c578063504006ca146100f757806368a2ddc6146101bc578063a88f231e146101d5578063f89dcbfd146101f3578063fda66f5414610206575b600080fd5b341561008757600080fd5b6100e560046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496505084359460208101359450604081013593506060810135925060800135905061021c565b60405190815260200160405180910390f35b341561010257600080fd5b61010d600435610456565b604051600160a060020a038816815260408101869052606081018590526080810184905260a0810183905260c0810182905260e06020820181815290820188818151815260200191508051906020019080838360005b8381101561017b578082015183820152602001610163565b50505050905090810190601f1680156101a85780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b34156101c757600080fd5b61010d6004356024356105d8565b34156101e057600080fd5b6101f16004356024356044356107b6565b005b34156101fe57600080fd5b6100e561087e565b341561021157600080fd5b6100e5600435610885565b6000610226610897565b6000806001871161023657600080fd5b60e06040519081016040908152600160a060020a0333168252602082018c905281018a9052606081018990526080810188905260a0810187905260c081018690526000805491945060019180830161028e83826108db565b600092835260209092208691600702018151815473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03919091161781556020820151816001019080516102e292916020019061090c565b5060408201516002820155606082015160038201556080820151816004015560a0820151816005015560c0820151816006015550500391508260a00151604051908152602001604051809103902090508160010160016000856060015184604051918252602082015260409081019051908190039020815260208101919091526040016000205560608301518351600160a060020a03167fdb0a3c9f19804ff8fa8903959afd142238f09eac5e6cd60086839c2b6606d6198560200151866040015187608001518860a001518960c001516040516020810185905260408101849052606081018390526080810182905260a08082528190810187818151815260200191508051906020019080838360005b8381101561040b5780820151838201526020016103f3565b50505050905090810190601f1680156104385780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a35098975050505050505050565b600061046061098a565b6000806000806000610470610897565b600080548a90811061047e57fe5b906000526020600020906007020160e060405190810160405290816000820160009054906101000a9004600160a060020a0316600160a060020a0316600160a060020a03168152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105605780601f1061053557610100808354040283529160200191610560565b820191906000526020600020905b81548152906001019060200180831161054357829003601f168201915b50505091835250506002820154602082015260038201546040820152600482015460608201526005820154608082015260069091015460a090910152905080519750806020015196508060400151955080606001519450806080015193508060a0015192508060c00151915050919395979092949650565b60006105e261098a565b60008060008060008060006105f5610897565b8a60405190815260200160405180910390209250600160008d85604051918252602082015260409081019051908190039020815260208101919091526040016000908120549250821161064757600080fd5b60008054600019840190811061065957fe5b906000526020600020906007020160e060405190810160405290816000820160009054906101000a9004600160a060020a0316600160a060020a0316600160a060020a03168152602001600182018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561073b5780601f106107105761010080835404028352916020019161073b565b820191906000526020600020905b81548152906001019060200180831161071e57829003601f168201915b50505091835250506002820154602082015260038201546040820152600482015460608201526005820154608082015260069091015460a090910152905080519950806020015198508060400151975080606001519650806080015195508060a0015194508060c00151935050505092959891949750929550565b6000808360405190815260200160405180910390209150600160008684604051918252602082015260409081019051908190039020815260208101919091526040016000908120549150811161080b57600080fd5b8260006001830381548110151561081e57fe5b600091825260209091206006600790920201015584600160a060020a0333167f88bc9b569bfef716340318ba28a625fc03144854b0ff470145b41c046f462aa0868660405191825260208201526040908101905180910390a35050505050565b6000545b90565b60016020526000908152604090205481565b60e060405190810160405260008152602081016108b261098a565b815260006020820181905260408201819052606082018190526080820181905260a09091015290565b81548183558181151161090757600702816007028360005260206000209182019101610907919061099c565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061094d57805160ff191683800117855561097a565b8280016001018555821561097a579182015b8281111561097a57825182559160200191906001019061095f565b50610986929150610a00565b5090565b60206040519081016040526000815290565b61088291905b8082111561098657805473ffffffffffffffffffffffffffffffffffffffff1916815560006109d46001830182610a1a565b5060006002820181905560038201819055600482018190556005820181905560068201556007016109a2565b61088291905b808211156109865760008155600101610a06565b50805460018160011615610100020316600290046000825580601f10610a405750610a5e565b601f016020900490600052602060002090810190610a5e9190610a00565b505600a165627a7a72305820797272700df5f0b8b125c2bb017a37407c0950d973a31c9311193ec7cac688a40029";

    protected Sol_LoanContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Sol_LoanContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<AddLoanEventResponse> getAddLoanEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("AddLoan", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<AddLoanEventResponse> responses = new ArrayList<AddLoanEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            AddLoanEventResponse typedResponse = new AddLoanEventResponse();
            typedResponse._sender = (Address) eventValues.getIndexedValues().get(0);
            typedResponse._identityHash = (Bytes32) eventValues.getIndexedValues().get(1);
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
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddLoanEventResponse>() {
            @Override
            public AddLoanEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                AddLoanEventResponse typedResponse = new AddLoanEventResponse();
                typedResponse._sender = (Address) eventValues.getIndexedValues().get(0);
                typedResponse._identityHash = (Bytes32) eventValues.getIndexedValues().get(1);
                typedResponse._customerName = (Utf8String) eventValues.getNonIndexedValues().get(0);
                typedResponse._idNumber = (Bytes32) eventValues.getNonIndexedValues().get(1);
                typedResponse._amount = (Uint256) eventValues.getNonIndexedValues().get(2);
                typedResponse._loanDate = (Uint256) eventValues.getNonIndexedValues().get(3);
                typedResponse._mortgageDate = (Uint256) eventValues.getNonIndexedValues().get(4);
                return typedResponse;
            }
        });
    }

    public List<UpdateLoanEventResponse> getUpdateLoanEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("UpdateLoan", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<UpdateLoanEventResponse> responses = new ArrayList<UpdateLoanEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            UpdateLoanEventResponse typedResponse = new UpdateLoanEventResponse();
            typedResponse._sender = (Address) eventValues.getIndexedValues().get(0);
            typedResponse._identityHash = (Bytes32) eventValues.getIndexedValues().get(1);
            typedResponse._loanDate = (Uint256) eventValues.getNonIndexedValues().get(0);
            typedResponse._mortgageDate = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UpdateLoanEventResponse> updateLoanEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("UpdateLoan", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, UpdateLoanEventResponse>() {
            @Override
            public UpdateLoanEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                UpdateLoanEventResponse typedResponse = new UpdateLoanEventResponse();
                typedResponse._sender = (Address) eventValues.getIndexedValues().get(0);
                typedResponse._identityHash = (Bytes32) eventValues.getIndexedValues().get(1);
                typedResponse._loanDate = (Uint256) eventValues.getNonIndexedValues().get(0);
                typedResponse._mortgageDate = (Uint256) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> addLoan(Utf8String _customerName, Bytes32 _idNumber, Bytes32 _nameAndNumber, Uint256 _amount, Uint256 _loanDate, Uint256 _mortgageDate) {
        Function function = new Function(
                "addLoan", 
                Arrays.<Type>asList(_customerName, _idNumber, _nameAndNumber, _amount, _loanDate, _mortgageDate), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256>> getLoan(Uint256 _index) {
        final Function function = new Function("getLoan", 
                Arrays.<Type>asList(_index), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256>>(
                new Callable<Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256>>() {
                    @Override
                    public Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);;
                        return new Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256>(
                                (Address) results.get(0), 
                                (Utf8String) results.get(1), 
                                (Bytes32) results.get(2), 
                                (Bytes32) results.get(3), 
                                (Uint256) results.get(4), 
                                (Uint256) results.get(5), 
                                (Uint256) results.get(6));
                    }
                });
    }

    public RemoteCall<Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256>> getLoanByHash(Bytes32 _hash, Uint256 _loanDate) {
        final Function function = new Function("getLoanByHash", 
                Arrays.<Type>asList(_hash, _loanDate), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256>>(
                new Callable<Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256>>() {
                    @Override
                    public Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);;
                        return new Tuple7<Address, Utf8String, Bytes32, Bytes32, Uint256, Uint256, Uint256>(
                                (Address) results.get(0), 
                                (Utf8String) results.get(1), 
                                (Bytes32) results.get(2), 
                                (Bytes32) results.get(3), 
                                (Uint256) results.get(4), 
                                (Uint256) results.get(5), 
                                (Uint256) results.get(6));
                    }
                });
    }

    public RemoteCall<TransactionReceipt> updateLoan(Bytes32 _hash, Uint256 _loanDate, Uint256 _mortgageDate) {
        Function function = new Function(
                "updateLoan", 
                Arrays.<Type>asList(_hash, _loanDate, _mortgageDate), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Uint256> getLoansLength() {
        Function function = new Function("getLoansLength", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Uint256> hashToIndex(Bytes32 param0) {
        Function function = new Function("hashToIndex", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public static RemoteCall<Sol_LoanContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sol_LoanContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Sol_LoanContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sol_LoanContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Sol_LoanContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sol_LoanContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Sol_LoanContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sol_LoanContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class AddLoanEventResponse {
        public Address _sender;

        public Bytes32 _identityHash;

        public Utf8String _customerName;

        public Bytes32 _idNumber;

        public Uint256 _amount;

        public Uint256 _loanDate;

        public Uint256 _mortgageDate;
    }

    public static class UpdateLoanEventResponse {
        public Address _sender;

        public Bytes32 _identityHash;

        public Uint256 _loanDate;

        public Uint256 _mortgageDate;
    }
}
