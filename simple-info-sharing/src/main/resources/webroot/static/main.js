const urlmapList = {
    "code": 200,
    "message": "成功",
    "data": [
        {
            "name": "python-spark-sql",
            "filename": "Python数据科学速查表 - Spark SQL 基础.pdf",
            "url": "/ct/python-spark-sql"
        },
        {
            "name": "lol",
            "filename": "死结 - 《英雄联盟》宇宙.html",
            "url": "/ct/lol"
        },
        {
            "name": "python-导入数据",
            "filename": "Python数据科学速查表 - 导入数据.pdf",
            "url": "/ct/python-导入数据"
        }
    ]
}

const fileList = {
    "code": 200,
    "message": "文件列表获取成功",
    "data": [
        {
            "filename": "ASMR(SUB)파티 의상이랑 악세서리까지 스타일링 해주는 메이크업샵 롤플레이_Doing your party makeup asmr - YouTube.html",
            "size": 217644527616,
            "url": "/content/ASMR(SUB)파티 의상이랑 악세서리까지 스타일링 해주는 메이크업샵 롤플레이_Doing your party makeup asmr - YouTube.html"
        },
        {
            "filename": "ASMR(SUB)파티 의상이랑 악세서리까지 스타일링 해주는 메이크업샵 롤플레이_Doing your party makeup asmr - YouTube_files",
            "size": 217644527616,
            "url": "/content/ASMR(SUB)파티 의상이랑 악세서리까지 스타일링 해주는 메이크업샵 롤플레이_Doing your party makeup asmr - YouTube_files"
        },
        {
            "filename": "Python数据科学速查表 - Jupyter Notebook.pdf",
            "size": 217644527616,
            "url": "/content/Python数据科学速查表 - Jupyter Notebook.pdf"
        },
        {
            "filename": "Python数据科学速查表 - Spark RDD 基础.pdf",
            "size": 217644527616,
            "url": "/content/Python数据科学速查表 - Spark RDD 基础.pdf"
        },
        {
            "filename": "Python数据科学速查表 - Spark SQL 基础.pdf",
            "size": 217644527616,
            "url": "/content/Python数据科学速查表 - Spark SQL 基础.pdf"
        },
        {
            "filename": "Python数据科学速查表 - 导入数据.pdf",
            "size": 217644527616,
            "url": "/content/Python数据科学速查表 - 导入数据.pdf"
        },
        {
            "filename": "死结 - 《英雄联盟》宇宙.html",
            "size": 217644527616,
            "url": "/content/死结 - 《英雄联盟》宇宙.html"
        },
        {
            "filename": "死结 - 《英雄联盟》宇宙_files",
            "size": 217644527616,
            "url": "/content/死结 - 《英雄联盟》宇宙_files"
        }
    ]
}

const uploadResponse = {
    "code": 200,
    "message": "文件上传成功",
    "data": {
        "Python数据科学速查表 - Jupyter Notebook.pdf": "OK"
    }
}


function merger(fileList, urlmapList) {
    let list = fileList.map(element => {
        let result = urlmapList.find(value => {
            return value.filename == element.filename
        })
        if (result != undefined) {
            element.name = result.name
            element.nameurl = result.url
        }
        return element
    })

    // 过滤文件夹文件
    list = list.filter(element => {
        return element.filename.includes(".")
    })

    return list
}

/** Restful API */

const URL_MAP_LIST_URL = '/api/urlmap/list'
const FILE_LIST_URL = '/api/file/list'
const FILE_UPLOAD_URL = "/api/file/upload"
const URL_MAP_PUT_URL = "/api/urlmap/put"
const URL_MAP_DEL_URL = "/api/urlmap/name/"

/** 初始化内容 list */
function initContentList() {
    let contentListTableVum = new Vue({
        el: "#content-list-table",
        data: {
            contentList: [],
            icon: {
                pdf: "far fa-file-pdf",
                file: "far fa-file",
                html: "fab fa-html5"
            }
        },
        methods: {
            iconClass: function (filename) {
                // console.log("filename = " + filename)
                filename = filename.toLowerCase()
                if (filename.endsWith(".pdf")) {
                    return [this.icon.pdf]
                } else if (filename.endsWith(".html")) {
                    return [this.icon.html]
                } else {
                    return [this.icon.file]
                }
            },
            flushContentList: function () {

                console.log("刷新内容列表")

                axios.all([
                    axios.get(URL_MAP_LIST_URL),
                    axios.get(FILE_LIST_URL)
                ]).then(axios.spread(function (urlmapList, fileList) {
                    let vm = contentListTableVum
                    vm.contentList = merger(fileList.data.data, urlmapList.data.data)
                    console.log("内容 list 数据加载成功")
                })).catch(err => {
                    console.log(err)
                })
            }
        },

        mounted: function () {
            this.flushContentList()
        }
    })

    return contentListTableVum
}


/** 初始化 index 页面 */
function initIndex() {
    initContentList()
}


/** 初始胡 manage 页面 */
function initManage() {

    let contentListTableVum = initContentList()

    // 上传文件
    let fileUploadVm = new Vue({
        el: "#file-upload",
        data: {
            file: "",
            filename: "",
            message: "",
            showMessage: false,
        },
        methods: {
            handleFiles: function (event) {
                this.file = event.target.files[0]
                this.filename = this.file.name
            },

            uploadFiles: function (event) {

                let param = new FormData();
                param.append('file', this.file);
                let config = {
                    headers: { 'Content-Type': 'multipart/form-data' }
                };
                axios.post(FILE_UPLOAD_URL, param, config)
                    .then(response => {
                        let data = response.data
                        let vm = fileUploadVm
                        vm.showMessage = true
                        vm.message = `<strong>${data.message}</strong><br>${Json2Html(data.data)}`

                        contentListTableVum.flushContentList()
                    }).catch(err => {
                        console.log(err)
                    })
            }
        },
    })

    // 添加新短名
    let urlmapPutVm = new Vue({
        el: "#urlmap-put",
        data: {
            filename: "",
            name: "",
            message: "",
            showMessage: false
        },
        methods: {
            addUrlMap: function (event) {

                let data = {
                    "filename": this.filename,
                    "name": this.name
                }
                let config = {
                    headers: { 'Content-Type': 'application/json' }
                };
                axios.post(URL_MAP_PUT_URL, data, config)
                    .then(res => {
                        let vm = urlmapPutVm
                        console.log(res.data)
                        vm.showMessage = true
                        vm.message = `<strong>${res.data.message}</strong><br>${vm.filename} -> ${vm.name}`
                        contentListTableVum.flushContentList()
                    }).catch(err => {
                        console.log(err)
                    })
            }
        }
    })

    //移除短名
    let urlmapDelVm = new Vue({
        el: "#urlmap-del",
        data: {
            name: "",
            message: "",
            showMessage: false
        },
        methods: {
            delUrlMap: function (event) {
                let url = URL_MAP_DEL_URL + this.name
                axios.delete(url)
                    .then(res => {
                        let vm = urlmapDelVm
                        if (res.data.code = 200) {
                            vm.showMessage = true
                            vm.message = `<strong>${res.data.message}</strong><br>name: ${vm.name}`
                        } else {
                            vm.showMessage = true
                            vm.message = `<strong>${res.data.message}</strong><br>name: ${vm.name}`
                        }
                        contentListTableVum.flushContentList()
                    }).catch(err => {
                        console.log(err)
                    })
            }
        }
    })
}


function Json2Html(data) {
    let html = ""
    for (const key in data) {
        html += key + ": " + data[key] + "<br>"
    }
    return html.substring(0, html.length - 4)
}





