/**
 * Fills the input fields of a form with the provided data.
 *
 * @param {HTMLFormElement} form - The HTMLFormElement representing the form.
 * @param {Record<string, string>} data - An object containing key-value pairs where the key is the name of the form control and the value is the data to be filled.
 * @returns void
 */
export function fillInputs(
  form: HTMLFormElement,
  data: Record<string, string>
) {
  for (const key in data) {
    const input = form.querySelector(
      `input[formcontrolname=${key}]`
    ) as HTMLInputElement;

    input.value = data[key];

    // * Notifies Angular that a change to one of the form fields was made
    input.dispatchEvent(new Event('input'));
  }
}
