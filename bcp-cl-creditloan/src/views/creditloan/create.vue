<template>
  <credit-loan-detail :is-edit="false"></credit-loan-detail>
</template>
<script>
  import CreditLoanDetail from './components/CreditLoanDetail'
  export default{
    name: 'createCreditLoan',
    components: {
      CreditLoanDetail
    }
  }
</script>
