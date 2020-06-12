from flask import Flask, jsonify, make_response
# from .service.country_service import isValidCountry
from service.country_service import isValidCountry

def createApp():
    app = Flask(__name__)
    app.config['JSON_AS_ASCII'] = False
    app.static_url_path = "/"
    return app

app = createApp()

# index.html 是在 static/文件夹里，这样直接当作静态文件返回它
# 而不是当成视图，因为 Jinja2 会对他进行渲染（你知道我们不需要它)
# 加载速度变慢不说，更重要的是会对 "{{ }}" 等插值表达式进行转义
# 但这些是对 VUE 准备的，不是 jinja2
@app.route('/', methods=['GET'])
def home():
    return app.send_static_file("index.html")


@app.route('/<string:country>', methods=['GET'])
def country(country):
    return app.send_static_file('country.html')

    # valid = isValidCountry(country)
    # if valid:
    #     print("country is {}".format(country))
    #     return app.send_static_file('country.html')
    # else:
    #     print("无效的国家名称: {}".format(country))
    #     return """
    #         <h2><strong>{}</strong>不是一个有效的国家名, iso2 或 iso3.<h2>
    #         """.format(country)


@app.route('/api/valid/<string:country>', methods=['GET'])
def validateCountry(country):
    valid = isValidCountry(country)
    return jsonify(country=country, valid=valid)

@app.route('/common.css')
def css():
    return app.send_static_file('common.css')

@app.route('/common.js')
def js():
    return app.send_static_file('common.js')


@app.route('/favicon.ico')
def favicon():
    return app.send_static_file('favicon.ico')



# 定义错误状态码404时，跳转的页面
@app.errorhandler(404)
def error( res ):
    return app.send_static_file("index.html")


if __name__ == "__main__":

    app.register_error_handler(404, error)
    app.run(debug=False)
