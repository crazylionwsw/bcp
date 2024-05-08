<template>
    <domesticoutfit-detail :is-edit="false"></domesticoutfit-detail>
</template>
<script>
  import DomesticoutfitDetail from './components/DomesticoutfitDetail'
  export default{
    name: 'createDomesticOutfit',
    components: {
      DomesticoutfitDetail
    }
  }
</script>
