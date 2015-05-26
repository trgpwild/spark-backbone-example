function htmlEncode(value) {
    return $('<div/>').text(value).html();
}

function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });
    return indexed_array;
}

var model_list_template_content = null;
var model_form_template_content = null;

$.get(model_list_template, function (content) {
    model_list_template_content = content;
});

$.get(model_form_template, function (content) {
    model_form_template_content = content;
});

$(function () {

    var Model = Backbone.Model.extend({
        urlRoot: url,
        defaults: defaults,
        validate: validate
    });

    var Models = Backbone.Collection.extend({
        url: url
    });

    var ModelListView = Backbone.View.extend({
        el: '.page',
        render: function () {
            var that = this;
            var modelsCollection = new Models();
            modelsCollection.fetch({
                success: function (models) {
                    var template = _.template(model_list_template_content, {
                        models: models.models
                    });
                    that.$el.html(template);
                }
            });
        }
    });
    var modelListView = new ModelListView();

    var ModelFormView = Backbone.View.extend({
        el: '.page',
        events: {
            'submit': 'save',
            'click .delete': 'deleteModel'
        },
        initialize: function () {
            _.bindAll(this, 'save');
        },
        save: function () {
            var me = this;
            this.model = new Model(getFormData($(document.formModel)));
            this.model.on('invalid', function (model, error) {
                me.showErrors(error);
            });
            if (this.model.isValid()) {
                this.model.save(null, {
                    success: function () {
                        router.navigate('', {trigger: true});
                        me.hideErrors();
                    },
                    error: function (model, response) {
                        if (onError)
                            onError(model, response);
                    },
                    wait: true
                });
            }
            return false;
        },
        deleteModel: function () {
            this.model.destroy({
                success: function () {
                    router.navigate('', {trigger: true});
                }
            });
            return false;
        },
        showErrors: function (errors) {
            _.each(errors, function (error) {
                var controlGroup = this.$('.' + error.name);
                controlGroup.addClass('error');
                controlGroup.find('.help-inline').text(error.message);
            }, this);
        },
        hideErrors: function () {
            this.$('.control-group').removeClass('error');
            this.$('.help-inline').text('');
        },
        render: function (options) {
            var that = this;
            if (options && options.id) {
                that.model = new Model({id: options.id});
                that.model.fetch({
                    success: function (model) {
                        var template = _.template(model_form_template_content, {model: model});
                        that.$el.html(template);
                    }
                });
            } else {
                var template = _.template(model_form_template_content, {model: null});
                that.$el.html(template);
            }
        }
    });
    var modelFormView = new ModelFormView();

    var Router = Backbone.Router.extend({
        routes: {
            "": "listagem",
            "alterar/:id": "alterar",
            "criar": "criar"
        }
    });

    var router = new Router();

    router.on('route:listagem', function () {
        modelListView.render();
    });

    router.on('route:alterar', function (id) {
        modelFormView.render({id: id});
    });

    router.on('route:criar', function () {
        modelFormView.render();
    });

    Backbone.history.start();

});