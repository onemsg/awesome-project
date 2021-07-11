"""
自动脚本工具
"""




def generateHtmlDom():
    
    DOM_STR = '<option value="{}">{}</option>'
    languages = ".properties Apache config Bash C C# C++ C-like foundation grammar for C/C++ grammars CSS CoffeeScript Diff Go HTML, XML HTTP JSON Java JavaScript Kotlin Less Lua Makefile Markdown Nginx config Objective-C PHP PHP Template Perl Plain text Python Python REPL Ruby Rust SCSS SQL Shell Session Swift TOML, also INI TypeScript YAML"
    dom = ""
    for language in languages.split():
        language = language.strip()
        tag = DOM_STR.format(language, language)
        dom += tag + "\n"
    print(dom)



if __name__ == "__main__":
    
    generateHtmlDom()
