// import DOMPurify from 'dompurify';
// include both this and DomPurity in the angular.json file
/*
"architect": {
        "build": {
        ...
        ...
            "scripts": [
              "node_modules/dompurify/dist/purify.js",
              "src/trusted-security-policies.js",
              "node_modules/bootstrap/dist/js/bootstrap.bundle.min.js"
            ],
         ...
         ...
 */
if (window.trustedTypes && window.trustedTypes.createPolicy) { // Feature testing
  window.trustedTypes.createPolicy('default', {
    createHTML: (string) => DOMPurify.sanitize(string, {RETURN_TRUSTED_TYPE: true}),
    createScriptURL: string => string, // warning: this is unsafe!
    createScript: string => string, // warning: this is unsafe!
  });
}
