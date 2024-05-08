<template>
  <credit-loan-detail :is-edit="true"></credit-loan-detail>
</template>
<script>
  import CreditLoanDetail from './components/CreditLoanDetail'
  export default{
    name: 'detailCreditLoan',
    components: {
      CreditLoanDetail
    }
  }
</script>
