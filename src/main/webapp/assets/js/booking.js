document.addEventListener('DOMContentLoaded', function () {

    const seatMap = document.getElementById('seatMap');
    const seatsPerRow = 10;
    const totalRows = Math.max(1, Math.ceil(ROOM_CAPACITY / seatsPerRow));
    const vipRowsCount = totalRows > 1 ? 2 : 0;
    const rowLetters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

    let seatsRemaining = ROOM_CAPACITY;
    const selectedSeats = new Set();

    for (let r = 0; r < totalRows; r++) {

        const seatsInThisRow = Math.min(seatsPerRow, seatsRemaining);
        if (seatsInThisRow <= 0) break;
        seatsRemaining -= seatsInThisRow;

        const rowDiv = document.createElement('div');
        rowDiv.className = 'seat-row';

        const rowChar = rowLetters[r] || ('R' + r);

        const label = document.createElement('span');
        label.className = 'row-label';
        label.textContent = rowChar;
        rowDiv.appendChild(label);

        const isVipRow = r >= totalRows - vipRowsCount;

        for (let s = 1; s <= seatsInThisRow; s++) {

            const seatId = rowChar + s;
            const seatIndex = r * seatsPerRow + s;
            const isSold = (seatIndex % 7 === 0);

            const seatDiv = document.createElement('div');
            seatDiv.className = 'seat available';
            if (isVipRow) seatDiv.classList.add('vip');

            if (isSold) {
                seatDiv.classList.remove('available');
                seatDiv.classList.add('sold');
            }

            seatDiv.textContent = seatId;
            seatDiv.dataset.seatId = seatId;

            if (!isSold) {
                seatDiv.addEventListener('click', function () {
                    toggleSeat(seatDiv, seatId);
                });
            }

            rowDiv.appendChild(seatDiv);
        }

        seatMap.appendChild(rowDiv);
    }

    function toggleSeat(seatDiv, seatId) {

        if (selectedSeats.has(seatId)) {
            selectedSeats.delete(seatId);
            seatDiv.classList.remove('selected');
        } else {
            selectedSeats.add(seatId);
            seatDiv.classList.add('selected');
        }

        updateSummary();
    }

    function updateSummary() {

        const count = selectedSeats.size;

        const seatCountEl = document.getElementById('seatCount');
        const seatsTextEl = document.getElementById('selectedSeatsText');
        const totalEl = document.getElementById('totalPrice');
        const confirmBtn = document.getElementById('confirmBtn');

        seatCountEl.textContent = count;

        seatsTextEl.textContent = count === 0
            ? 'Chưa chọn ghế'
            : Array.from(selectedSeats).sort().join(', ');

        const total = count * TICKET_PRICE;
        totalEl.textContent = total.toLocaleString('vi-VN') + 'đ';

        confirmBtn.disabled = count === 0;
    }

    document.getElementById('cancelBtn').addEventListener('click', function () {

        selectedSeats.forEach(function (seatId) {
            const el = seatMap.querySelector('[data-seat-id="' + seatId + '"]');
            if (el) el.classList.remove('selected');
        });

        selectedSeats.clear();
        updateSummary();
    });

    document.getElementById('confirmBtn').addEventListener('click', function () {

        if (selectedSeats.size === 0) return;

        alert(
            'Đặt vé thành công cho ghế: ' +
            Array.from(selectedSeats).sort().join(', ') +
            '\n(Đây là bản demo giao diện, chưa lưu vào hệ thống)'
        );
    });

    updateSummary();
});