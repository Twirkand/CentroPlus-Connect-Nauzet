window.onload = function() {
    const token = sessionStorage.getItem('swaggerToken');

    const ui = SwaggerUIBundle({
        url: '/v3/api-docs',
        dom_id: '#swagger-ui',
        presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],
        layout: 'StandaloneLayout',
        onComplete: function() {
            if (token) {
                ui.preauthorizeApiKey('Bearer Token', token);
            }
        }
    });

    window.ui = ui;
};