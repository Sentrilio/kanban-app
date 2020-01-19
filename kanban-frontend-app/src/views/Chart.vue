<template>
  <div class="chart">
    <button class="btn" @click="handleBoardClick">
      <font-awesome-icon icon="arrow-left" />
      {{boardName}}
    </button>
    <div class="chart-area" v-if="this.render">
      <apexchart type="area" height="550" :options="chartOptions" :series="series"></apexchart>
    </div>
  </div>
</template>
<script>
import TrendService from "../services/TrendService";
export default {
  data() {
    return {
      boardName: "",
      boardId: "",
      render: false,
      trends: {},
      series: [
        // {
        //   name: "series1",
        //   data: [31, 40, 28, 51, 42, 109, 100]
        // },
        // {
        //   name: "series2",
        //   data: [11, 32, 45, 32, 34, 52, 41]
        // }
      ],
      chartOptions: {
        chart: {
          toolbar: {
            show: true,
            tools: {
              download: true,
              selection: true,
              zoom: true,
              zoomin: true,
              zoomout: true,
              pan: false,
              reset: true | '<img src="/static/icons/reset.png" width="20">',
              // customIcons: []
            },
            autoSelected: "zoom"
          },
          height: 350,
          type: "area"
          // type: "line"
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: "smooth"
          // curve: "straight"
        },
        legend: {
          fontSize: "15px"
        },
        xaxis: {
          type: "datetime",
          categories: [],
          // tickPlacement: "between",
          //   "2018-09-19T00:00:00.000Z",

          labels: {
            style: {
              fontSize: "15px"
              // margin: "15px",
            }
            // datetimeFormatter: {
            // year: "yyyy",
            // month: "MMM 'yy",
            // day: "dd MMM",
            // hour: "HH:mm"
            // }
          }
          // tickAmount: 2,
        }
        // tooltip: {
        // x: {
        // format: "yyyy/MM/dd HH:mm"
        // format: "dd/MM/yy HH:mm"
        // }
        // }
      }
    };
  },

  methods: {
    handleBoardClick() {
      let boardId = this.boardId;
      let boardName = this.boardName;
      this.$router.push({
        name: "board",
        params: { boardId, boardName }
      });
    },
    getTrends() {
      let boardId = this.boardId;
      this.boardName = this.boardName;
      let data = {
        boardId: boardId,
        days: 4
      };
      TrendService.getTrends(data)
        .then(response => {
          this.trends = response.data;
          this.series = this.trends.seriesList;
          this.chartOptions.xaxis.categories = this.trends.dates;
          this.render = true;
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  created() {
    this.boardId = this.$route.params.boardId;
    this.boardName = this.$route.params.boardName;
    this.getTrends();
  }
};
</script>
<style lang="css" scoped>
button {
  margin-left: 10px;
  background-color: #ebebe0;
  margin-top: 10px;
}
.chart{
  width: 1500px;
}
</style>