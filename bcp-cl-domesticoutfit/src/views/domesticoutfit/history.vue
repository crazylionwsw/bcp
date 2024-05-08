<template>
  <domesticoutfit-list :is-history="true"></domesticoutfit-list>
</template>
<script>
  import DomesticoutfitList from './components/DomesticoutfitList'
  export default{
    name: 'domesticOutfitHistory',
    components: {
      DomesticoutfitList
    }
  }
</script>
