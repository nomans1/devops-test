{{/*
Common labels 
*/}}
{{- define "mychart.labels" -}}
{{ include "mychart.governanceLabels" . }}
{{ include "mychart.helmLabels" . }}
{{- end -}}

{{/*
Selector labels for Helm
*/}}
{{- define "mychart.helmLabels" -}}
helm.sh/chart: {{.Chart.Name}}-{{.Chart.Version | replace "+" "_" }}
app.kubernetes.io/name: {{ .Values.name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}




