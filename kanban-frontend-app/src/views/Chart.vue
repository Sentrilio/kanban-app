<template>
  <div class="chart">
    <button class="btn" @click="handleBoardClick">
      <font-awesome-icon icon="arrow-left" />
      {{boardName}}
    </button>
    <div class="chart-area" v-if="this.render">
      <apexchart type="line" height="550" :options="chartOptions" :series="series"></apexchart>
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
          defaultLocale: "pl",
          locales: [
            {
              name: "pl",
              options: {
                months: [
                  "Styczeń",
                  "Luty",
                  "Marzec",
                  "Kwiecień",
                  "Maj",
                  "Czerwiec",
                  "Lipiec",
                  "Sierpień",
                  "Wrzesień",
                  "Październik",
                  "Listopad",
                  "Grudzień"
                ],
                shortMonths: [
                  "Sty",
                  "Lut",
                  "Mar",
                  "Kwi",
                  "Maj",
                  "Cze",
                  "Lip",
                  "Sie",
                  "Wrz",
                  "Paź",
                  "Lis",
                  "Gru"
                ],
                days: [
                  "Niedziela",
                  "Poniedziałek",
                  "Wtorek",
                  "Środa",
                  "Czwartek",
                  "Piątek",
                  "Sobota"
                ],
                shortDays: ["Nie", "Pon", "Wto", "Śro", "Cz", "Pią", "Sob"],
                toolbar: {
                  download: "Pobierz SVG",
                  selection: "Zaznaczenie",
                  selectionZoom: "Przybliż Zaznaczenie",
                  zoomIn: "Przybliż",
                  zoomOut: "Oddal",
                  pan: "Panning",
                  reset: "Zresetuj przybliżenie"
                }
              }
            }
          ],
          toolbar: {
            show: true,
            tools: {
              download: true,
              selection: true,
              zoom: true,
              zoomin: true,
              zoomout: true,
              pan: false,
              reset: true | '<img src="/static/icons/reset.png" width="20">'
              // customIcons: []
            },
            autoSelected: "zoom"
          },
          // stacked: true,
          height: 350
          // type: "area"
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          // curve: "smooth",
          curve: "straight"
        },
        legend: {
          fontSize: "15px"
        },
        colors: [
          "#F3B415",
          "#00A7C6",
          "#663F59",
          "#46AF78",
          "#F27036",
          "#000000",
         "#00FF00",
       
          
          
          "#2176FF",
          "#33A1FD",
          "#7A918D",
          "#BAFF29",
          "#6A6E94",
          "#4E88B4",
           "#18D8D8",
              "#A9D794",
              "#A93F55",
              "#8C5E58",
        ],
        yaxis: {
          // floating:false,
          decimalsInFloat: false
          // allowDecimals: false,
        },
        xaxis: {
          type: "datetime",
          categories: [],
          labels: {
            style: {
              fontSize: "15px"
            }
          }
        }
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
      TrendService.getTrends(boardId)
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
.chart {
  width: 1500px;
}
</style>