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
public class Sol_LoanContract3 extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b61095d8061001e6000396000f3006060604052600436106100775763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166330928664811461007c578063504006ca146100f757806368a2ddc6146101bc578063a88f231e146101d5578063f89dcbfd146101f3578063fda66f5414610206575b600080fd5b341561008757600080fd5b6100e560046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496505084359460208101359450604081013593506060810135925060800135905061021c565b60405190815260200160405180910390f35b341561010257600080fd5b61010d60043561043d565b604051600160a060020a038816815260408101869052606081018590526080810184905260a0810183905260c0810182905260e06020820181815290820188818151815260200191508051906020019080838360005b8381101561017b578082015183820152602001610163565b50505050905090810190601f1680156101a85780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b34156101c757600080fd5b61010d600435602435610557565b34156101e057600080fd5b6101f16004356024356044356106a6565b005b34156101fe57600080fd5b6100e561074e565b341561021157600080fd5b6100e5600435610755565b6000610226610767565b60006001861161023557600080fd5b60e06040519081016040908152600160a060020a0333168252602082018b90528101899052606081018890526080810187905260a0810186905260c081018590526000805491935060019180830161028d83826107ab565b600092835260209092208591600702018151815473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03919091161781556020820151816001019080516102e19291602001906107dc565b5060408201516002820155606082015160038201556080820151816004015560a0820151816005015560c082015181600601555050039050806001600084606001518560a00151604051918252602082015260409081019051908190039020815260208101919091526040016000205560608201518251600160a060020a03167fdb0a3c9f19804ff8fa8903959afd142238f09eac5e6cd60086839c2b6606d6198460200151856040015186608001518760a001518860c001516040516020810185905260408101849052606081018390526080810182905260a08082528190810187818151815260200191508051906020019080838360005b838110156103f35780820151838201526020016103db565b50505050905090810190601f1680156104205780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a398975050505050505050565b600061044761085a565b60008060008060008060008981548110151561045f57fe5b906000526020600020906007020190508060000160009054906101000a9004600160a060020a03169750806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105215780601f106104f657610100808354040283529160200191610521565b820191906000526020600020905b81548152906001019060200180831161050457829003601f168201915b50505050509650806002015495508060030154945080600401549350806005015492508060060154915050919395979092949650565b600061056161085a565b6000806000806000806000600160008c8c604051918252602082015260409081019051908190039020815260208101919091526040016000908120548154909350839081106105ac57fe5b906000526020600020906007020190508060000160009054906101000a9004600160a060020a03169850806001018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561066e5780601f106106435761010080835404028352916020019161066e565b820191906000526020600020905b81548152906001019060200180831161065157829003601f168201915b505050505097508060020154965080600301549550806004015494508060050154935080600601549250505092959891949750929550565b600080600160008686604051918252602082015260409081019051908190039020815260208101919091526040016000908120548154909350839081106106e957fe5b60009182526020909120600790910201600681018490556003810154815491925090600160a060020a03167f46fcec934b5ed67f40b4ee0bc359b5886e575070d18f92dd4d9e889d814eae178560405190815260200160405180910390a35050505050565b6000545b90565b60016020526000908152604090205481565b60e0604051908101604052600081526020810161078261085a565b815260006020820181905260408201819052606082018190526080820181905260a09091015290565b8154818355818115116107d7576007028160070283600052602060002091820191016107d7919061086c565b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061081d57805160ff191683800117855561084a565b8280016001018555821561084a579182015b8281111561084a57825182559160200191906001019061082f565b506108569291506108d0565b5090565b60206040519081016040526000815290565b61075291905b8082111561085657805473ffffffffffffffffffffffffffffffffffffffff1916815560006108a460018301826108ea565b506000600282018190556003820181905560048201819055600582018190556006820155600701610872565b61075291905b8082111561085657600081556001016108d6565b50805460018160011615610100020316600290046000825580601f10610910575061092e565b601f01602090049060005260206000209081019061092e91906108d0565b505600a165627a7a72305820e4b5a1bf77e6d7c2ceae171fa5ff9d96fc85ba6c1670308538b441ad23efad9f0029";

    protected Sol_LoanContract3(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Sol_LoanContract3(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
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
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<UpdateLoanEventResponse> responses = new ArrayList<UpdateLoanEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            UpdateLoanEventResponse typedResponse = new UpdateLoanEventResponse();
            typedResponse._sender = (Address) eventValues.getIndexedValues().get(0);
            typedResponse._identityHash = (Bytes32) eventValues.getIndexedValues().get(1);
            typedResponse._mortgageDate = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UpdateLoanEventResponse> updateLoanEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("UpdateLoan", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, UpdateLoanEventResponse>() {
            @Override
            public UpdateLoanEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                UpdateLoanEventResponse typedResponse = new UpdateLoanEventResponse();
                typedResponse._sender = (Address) eventValues.getIndexedValues().get(0);
                typedResponse._identityHash = (Bytes32) eventValues.getIndexedValues().get(1);
                typedResponse._mortgageDate = (Uint256) eventValues.getNonIndexedValues().get(0);
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

    public static RemoteCall<Sol_LoanContract3> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sol_LoanContract3.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Sol_LoanContract3> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sol_LoanContract3.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Sol_LoanContract3 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sol_LoanContract3(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Sol_LoanContract3 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sol_LoanContract3(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
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

        public Uint256 _mortgageDate;
    }
}
