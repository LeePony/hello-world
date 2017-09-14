var del        = require('del');
var gulp       = require('gulp');
var sass       = require('gulp-ruby-sass');
var filter     = require('gulp-filter');
var rename     = require('gulp-rename');
var concat     = require('gulp-concat');
var uglify     = require('gulp-uglify');
var prefixer   = require('gulp-autoprefixer');
var cleanCSS   = require('gulp-clean-css');
var sourcemaps = require('gulp-sourcemaps');
var amdOptimize = require('amd-optimize');

// 应当兼容的浏览器版本
var compatBrowser = ['last 2 versions', '> 1%', 'ie >= 8'];

// 删除生成的样式文件
gulp.task('clean', function() {
    del('./css');
});

// 编译sass样式
gulp.task('compile', ['clean'], function() {
    return sass('./sass', { style: 'expanded', sourcemap: true })
        .on('error', sass.logError)
        .pipe(prefixer({ browsers: compatBrowser }))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('./css'))
        .pipe(filter('**/*.css'))
        .pipe(cleanCSS())
        .pipe(rename({ suffix: '.min' }))
        .pipe(gulp.dest('./css'))
});


// 监听变化并自动编译
gulp.task('watch', function() {
    gulp.watch('./sass/**/*.scss', ['compile']);
});


// 默认任务：监听变化并自动编译
gulp.task('default', ['compile'], function() {
    gulp.start('watch');
});
