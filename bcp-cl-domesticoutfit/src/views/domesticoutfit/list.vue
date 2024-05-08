<template>
  <domesticoutfit-list :is-history="false"></domesticoutfit-list>
</template>
<script>
    import DomesticoutfitList from './components/DomesticoutfitList'
    export default{
      name: 'domesticOutfitList',
      components: {
        DomesticoutfitList
      }
    }
</script>
