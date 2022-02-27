/** 公共 JS */

/** 疫情数据 RESTful API */
const GLOBA_URL = "https://corona.lmao.ninja/v2/all"
const GLOVA_HISTORICAL_URL = "https://corona.lmao.ninja/v2/historical/all"
const COUNTRIES_URL = "https://corona.lmao.ninja/v2/countries"
const COUNTRIES_SORT_BY_CASES_URL = "https://corona.lmao.ninja/v2/countries?sort=cases"
const COUNTRIY_URL = "https://corona.lmao.ninja/v2/countries/" //:countryName
const COUNTRIES_SORT_URL = "https://corona.lmao.ninja/v2/countries?sort="  //{parameter}
const COUNTRIY_HISTORICAL_URL = "https://corona.lmao.ninja/v2/historical/"  //:country
const COUNTRIY_PROVINCE_HISTORICAL_URL = "https://corona.lmao.ninja/v2/historical/" //:country/:province


/** 返回一个 echart 实例，方便配置 */
function getEchart(el){
    return echarts.init(document.getElementById(el))
}

/** 返回一个折线图基本配置参数 */
function getBaseOption(title="", date=[], data=[]){

    return {
        title: {
            text: title,
            x: "center"
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
            },
            formatter: function (params) {
                params = params[0];
                var date = new Date(params.name);
                return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value;
            },
        },
        xAxis: {
            name: '日期',
            type: 'category',
            data: date,
            show: true,
        },
        yAxis: {
            type: 'value',
            name: '人数',
            axisLabel: {
                show: true,
                formatter: function(value, index){
                    return value / 1000 + "k"
                }
            },
            axisTick: {
                show: false
            }
        },
        series: {
            type: "line",
            data: data,
            // smooth: true,
            showSymbol: false,
            hoverAnimation: false,
            animationDelay: function (idx) {
                return idx * 10;
            }
        },
        animationEasing: 'elasticOut',
        animationDelayUpdate: function (idx) {
            return idx * 5;
        }
    }
}

/** 计算每日新增数据 */
function getDailyData(data){
    let dates = Object.keys(data)
    let values = Object.values(data)
    let daily_data = {}
    for(let i=1; i<dates.length; i++){
        value = values[i] - values[i-1]
        daily_data[dates[i]] = value 
    }
    return daily_data
}

// daily_data = getDailyData(TEST_DATA.deaths)
// console.log(daily_data)

/**
 * 全球最近30天 感染人数 | 死亡人数 | 每日新增 | 每日死亡
 * 
 * 折线图|条形图 - 格式：x轴：日期，y轴：数量，提供线性和对数方式
 *  */
function initGlobaHisChart(url){
    let chart_cases = getEchart("chart-his-cases")
    let chart_deaths = getEchart("chart-his-deaths")
    let chart_recoverd = getEchart("chart-his-recovered")
    let chart_daily_cases = getEchart("chart-his-daily-cases")
    let chart_daily_deaths = getEchart("chart-his-daily-deaths")

    let option_cases = getBaseOption("HISTORICAL OF CASES")
    let option_deaths = getBaseOption("HISTORICAL OF DEATHS")
    let option_recoverd = getBaseOption("HISTORICAL OF RECOVERD")
    let option_daily_cases = getBaseOption("HISTORICAL OF DAILY CASES")
    let option_daily_deaths = getBaseOption("HISTORICAL OF DAILY DEATHS")

    chart_cases.setOption(option_cases)
    chart_deaths.setOption(option_deaths)
    chart_recoverd.setOption(option_recoverd)
    chart_daily_cases.setOption(option_daily_cases)
    chart_daily_deaths.setOption(option_daily_deaths)

    axios.get(url).then(res => {

        let data = res.data

        option_cases.series.data = Object.values(data['cases'])
        option_cases.xAxis.data = Object.keys(data['cases'])

        option_deaths.series.data = Object.values(data['deaths'])
        option_deaths.xAxis.data = Object.keys(data['deaths'])

        option_recoverd.series.data = Object.values(data['recovered'])
        option_recoverd.xAxis.data = Object.keys(data['recovered'])
        
        let daily_cases = getDailyData(data['cases'])
        let daily_deaths = getDailyData(data['deaths'])

        option_daily_cases.series.data = Object.values(daily_cases)
        option_daily_cases.xAxis.data = Object.keys(daily_cases)
        option_daily_cases.series.type = "bar"

        option_daily_deaths.series.data = Object.values(daily_deaths)
        option_daily_deaths.xAxis.data = Object.keys(daily_deaths)
        option_daily_deaths.series.type = "bar"

        chart_cases.setOption(option_cases)
        chart_deaths.setOption(option_deaths)
        chart_recoverd.setOption(option_recoverd)
        chart_daily_cases.setOption(option_daily_cases)
        chart_daily_deaths.setOption(option_daily_deaths)
    })
}

/**
 * 国家最近30天 感染人数 | 死亡人数 | 每日新增 | 每日死亡
 * 
 * 折线图|条形图 - 格式：x轴：日期，y轴：数量，提供线性和对数方式
 *  */
function initCountryHisChart(url) {
    let chart_cases = getEchart("chart-his-cases")
    let chart_deaths = getEchart("chart-his-deaths")
    let chart_recoverd = getEchart("chart-his-recovered")
    let chart_daily_cases = getEchart("chart-his-daily-cases")
    let chart_daily_deaths = getEchart("chart-his-daily-deaths")

    let option_cases = getBaseOption("HISTORICAL OF CASES")
    let option_deaths = getBaseOption("HISTORICAL OF DEATHS")
    let option_recoverd = getBaseOption("HISTORICAL OF RECOVERD")
    let option_daily_cases = getBaseOption("HISTORICAL OF DAILY CASES")
    let option_daily_deaths = getBaseOption("HISTORICAL OF DAILY DEATHS")

    chart_cases.setOption(option_cases)
    chart_deaths.setOption(option_deaths)
    chart_recoverd.setOption(option_recoverd)
    chart_daily_cases.setOption(option_daily_cases)
    chart_daily_deaths.setOption(option_daily_deaths)

    axios.get(url).then(res => {

        let data = res.data.timeline

        option_cases.series.data = Object.values(data['cases'])
        option_cases.xAxis.data = Object.keys(data['cases'])

        option_deaths.series.data = Object.values(data['deaths'])
        option_deaths.xAxis.data = Object.keys(data['deaths'])

        option_recoverd.series.data = Object.values(data['recovered'])
        option_recoverd.xAxis.data = Object.keys(data['recovered'])

        let daily_cases = getDailyData(data['cases'])
        let daily_deaths = getDailyData(data['deaths'])

        option_daily_cases.series.data = Object.values(daily_cases)
        option_daily_cases.xAxis.data = Object.keys(daily_cases)
        option_daily_cases.series.type = "bar"

        option_daily_deaths.series.data = Object.values(daily_deaths)
        option_daily_deaths.xAxis.data = Object.keys(daily_deaths)
        option_daily_deaths.series.type = "bar"

        chart_cases.setOption(option_cases)
        chart_deaths.setOption(option_deaths)
        chart_recoverd.setOption(option_recoverd)
        chart_daily_cases.setOption(option_daily_cases)
        chart_daily_deaths.setOption(option_daily_deaths)
    })
}



//
// VUE 组件
//

/** 全球 / 国家 各国家 / 州（省）详细信息 */
function initDetailDataTable() {
    let el = "#detail-data-table"
    let detailDataTableVm = new Vue({
        el: el,
        data: {
            detailData: [],
            showProgress: true
        },
        methods: {
            sortData: function (event, param, ascending){
                if(ascending){
                    this.detailData.sort( (a,b) =>  { a[parm] - b[parm]} )
                }else{
                    this.detailData.sort((a, b) => { b[parm] - a[parm] })
                }
            }
        },
        mounted: function () {
            axios.get(COUNTRIES_SORT_BY_CASES_URL)
                .then( res => {
                    let vm = detailDataTableVm
                    vm.showProgress = false
                    vm.detailData = res.data
                })
                .catch( err => {
                    console.log(err)
                })
        }
    })
}


function initBaseInfo(url) {
    let el = "#base-info"
    let baseInfoVm = new Vue({
        el: el,
        data: {
            baseData: {},
            lastUpdate: ""
        },
        mounted: function () {
            axios.get(url)
                .then(res => {
                    let vm = baseInfoVm
                    vm.baseData = res.data
                    vm.lastUpdate = new Date(res.data.updated)
                })
                .catch(err => {
                    console.log(err)
                })
        }
    })
}


