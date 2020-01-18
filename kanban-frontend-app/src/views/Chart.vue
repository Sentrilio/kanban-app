<template>
  <div>
    <div v-if="this.render">
      <apexchart type="area" height="350" :options="chartOptions" :series="series"></apexchart>
    </div>
  </div>
</template>
<script>
import TrendService from "../services/TrendService";
export default {
  data() {
    return {
      render: false,
      trends: {},
      series: [
        {
          name: "series1",
          data: [31, 40, 28, 51, 42, 109, 100]
        },
        {
          name: "series2",
          data: [11, 32, 45, 32, 34, 52, 41]
        }
      ],
      chartOptions: {
        chart: {
          height: 350,
          type: "area"
        },
        dataLabels: {
          // enabled: true
          enabled: false

        },
        stroke: {
          curve: "smooth"
        },
        xaxis: {
          type: "datetime",
          // categories: [
          //   "2018-09-19T00:00:00.000Z",
          //   "2018-09-19T01:30:00.000Z",
          //   "2018-09-19T02:30:00.000Z",
          //   "2018-09-19T03:30:00.000Z",
          //   "2018-09-19T04:30:00.000Z",
          //   "2018-09-19T05:30:00.000Z",
          //   "2018-09-19T06:30:00.000Z"
          // ],
          labels: {
            datetimeFormatter: {
              year: "yyyy",
              month: "MMM 'yy",
              day: "dd MMM",
              hour: "HH:mm"
            }
          }
        },
        // tooltip: {
        //   x: {
        //     format: "yyyy/MM/dd HH:mm"
        //     // format: "dd/MM/yy HH:mm"
        //   }
        // }
      }
    };
  },
  components: {
    // GChart
    // apexchart: VueApexCharts,
  },

  methods: {
    getTrends() {
      TrendService.getTrends(this.$route.params.boardId)
        .then(response => {
          this.trends = response.data;
          this.series = this.trends.seriesList;
          this.chartOptions.xaxis.categories = this.trends.dates;

          // this.series = response.data.seriesList;
          // this.chartOptions.xaxis.categories = response.data.dates;
          // this.trends = response.data;
          this.render = true;
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  created() {
    this.getTrends();
  }
};
</script>
<style lang="css" scoped>
</style>