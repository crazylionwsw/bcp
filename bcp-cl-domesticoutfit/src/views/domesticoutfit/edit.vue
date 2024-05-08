<template>
  <domesticoutfit-detail :is-edit="true"></domesticoutfit-detail>
</template>
<script>
  import DomesticoutfitDetail from './components/DomesticoutfitDetail'
  export default{
    name: 'editDomesticOutfit',
    components: {
      DomesticoutfitDetail
    }
  }
</script>
